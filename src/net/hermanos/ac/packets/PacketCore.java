package net.hermanos.ac.packets;

import com.comphenix.protocol.*;
import org.bukkit.*;
import org.bukkit.event.*;
import com.comphenix.protocol.events.*;
import org.bukkit.entity.*;
import java.util.*;
import com.comphenix.protocol.wrappers.*;

import net.hermanos.ac.*;
import net.hermanos.ac.packets.events.*;

public class PacketCore
{
    public Bernard Daedalus;
    private HashSet<EntityType> enabled;
    public Map<UUID, Integer> movePackets;
    private static final PacketType[] ENTITY_PACKETS;
    
    static {
        ENTITY_PACKETS = new PacketType[] { PacketType.Play.Server.SPAWN_ENTITY_LIVING, PacketType.Play.Server.NAMED_ENTITY_SPAWN, PacketType.Play.Server.ENTITY_METADATA };
    }
    
    public PacketCore(final Bernard Daedalus) {
        this.Daedalus = Daedalus;
        (this.enabled = new HashSet<EntityType>()).add(EntityType.valueOf("PLAYER"));
        this.movePackets = new HashMap<UUID, Integer>();
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.USE_ENTITY }) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                try {
                    final Object playEntity = PacketCore.this.getNMSClass("PacketPlayInUseEntity");
                    final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
                    if (version.contains("1_7")) {
                        if (packet.getHandle() == playEntity && playEntity.getClass().getMethod("c", (Class<?>[])new Class[0]) == null) {
                            return;
                        }
                    }
                    else if (packet.getHandle() == playEntity && playEntity.getClass().getMethod("a", (Class<?>[])new Class[0]) == null) {
                        return;
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                EnumWrappers.EntityUseAction type;
                try {
                    type = (EnumWrappers.EntityUseAction)packet.getEntityUseActions().read(0);
                }
                catch (Exception ex) {
                    return;
                }
                final int entityId = (int)packet.getIntegers().read(0);
                Entity entity = null;
                for (final Entity entityentity : player.getWorld().getEntities()) {
                    if (entityentity.getEntityId() == entityId) {
                        entity = entityentity;
                    }
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketUseEntityEvent(type, player, entity));
                if (type == EnumWrappers.EntityUseAction.ATTACK) {
                    Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKillauraEvent(player, PacketPlayerType.USE));
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(Daedalus, PacketCore.ENTITY_PACKETS) {
            public void onPacketSending(final PacketEvent event) {
                PacketContainer packet = event.getPacket();
                final Entity e = (Entity)packet.getEntityModifier(event).read(0);
                if (e instanceof LivingEntity && PacketCore.this.enabled.contains(e.getType()) && packet.getWatchableCollectionModifier().read(0) != null && e.getUniqueId() != event.getPlayer().getUniqueId()) {
                    packet = packet.deepClone();
                    event.setPacket(packet);
                    if (event.getPacket().getType() == PacketType.Play.Server.ENTITY_METADATA) {
                        @SuppressWarnings({ "unchecked", "rawtypes" })
						final WrappedDataWatcher watcher = new WrappedDataWatcher((List)packet.getWatchableCollectionModifier().read(0));
                        this.processDataWatcher(watcher);
                        packet.getWatchableCollectionModifier().write(0, (List<WrappedWatchableObject>)watcher.getWatchableObjects());
                    }
                }
            }
            
            private void processDataWatcher(final WrappedDataWatcher watcher) {
                if (watcher != null && watcher.getObject(6) != null && watcher.getFloat(6) != 0.0f) {
                    watcher.setObject(6, (Object)1.0f);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.POSITION_LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (double)event.getPacket().getDoubles().read(0), (double)event.getPacket().getDoubles().read(1), (double)event.getPacket().getDoubles().read(2), (float)event.getPacket().getFloat().read(0), (float)event.getPacket().getFloat().read(1), PacketPlayerType.POSLOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.LOOK }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (double)event.getPacket().getDoubles().read(0), (double)event.getPacket().getDoubles().read(1), (double)event.getPacket().getDoubles().read(2), (float)event.getPacket().getFloat().read(0), (float)event.getPacket().getFloat().read(1), PacketPlayerType.POSLOOK));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.POSITION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, (double)event.getPacket().getDoubles().read(0), (double)event.getPacket().getDoubles().read(1), (double)event.getPacket().getDoubles().read(2), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketPlayerType.POSITION));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Server.POSITION }) {
            public void onPacketSending(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                int i = PacketCore.this.movePackets.getOrDefault(player.getUniqueId(), 0);
                ++i;
                PacketCore.this.movePackets.put(player.getUniqueId(), i);
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.ENTITY_ACTION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final PacketContainer packet = event.getPacket();
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketEntityActionEvent(player, (int)packet.getIntegers().read(1)));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.KEEP_ALIVE }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKeepAliveEvent(player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.ARM_ANIMATION }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketKillauraEvent(player, PacketPlayerType.ARM_SWING));
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketSwingArmEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.HELD_ITEM_SLOT }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketHeldItemChangeEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.BLOCK_PLACE }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketBlockPlacementEvent(event, player));
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener((PacketListener)new PacketAdapter(this.Daedalus, new PacketType[] { PacketType.Play.Client.FLYING }) {
            public void onPacketReceiving(final PacketEvent event) {
                final Player player = event.getPlayer();
                if (player == null) {
                    return;
                }
                Bukkit.getServer().getPluginManager().callEvent((Event)new PacketPlayerEvent(player, player.getLocation().getX(), player.getLocation().getY(), player.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch(), PacketPlayerType.FLYING));
            }
        });
    }
    
    public Class<?> getNMSClass(final String name) {
        final String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            return Class.forName("net.minecraft.server." + version + "." + name);
        }
        catch (ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
