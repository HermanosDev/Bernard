package net.hermanos.ac.packets.events;

public enum PacketPlayerType
{
    POSLOOK("POSLOOK", 0), 
    POSITION("POSITION", 1), 
    LOOK("LOOK", 2), 
    FLYING("FLYING", 3), 
    ARM_SWING("ARM_SWING", 4), 
    USE("USE", 5);
    
    private PacketPlayerType(final String s, final int n) {
    }
}
