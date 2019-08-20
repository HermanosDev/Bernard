package net.hermanos.ac.check;

import org.bukkit.entity.*;
import org.bukkit.event.*;

import net.hermanos.ac.*;
import net.hermanos.ac.utils.*;

import java.util.*;

public class Check implements Listener
{
    private String Identifier;
    private String Name;
    private Bernard Daedalus;
    private boolean Enabled;
    private boolean BanTimer;
    private boolean Bannable;
    private boolean JudgementDay;
    private Integer MaxViolations;
    private Integer ViolationsToNotify;
    private Long ViolationResetTime;
    public Map<String, List<String>> DumpLogs;
    
    public Check(final String Identifier, final String Name, final Bernard Daedalus) {
        this.Enabled = true;
        this.BanTimer = false;
        this.Bannable = true;
        this.JudgementDay = false;
        this.MaxViolations = 5;
        this.ViolationsToNotify = 1;
        this.ViolationResetTime = 600000L;
        this.DumpLogs = new HashMap<String, List<String>>();
        this.Name = Name;
        this.Daedalus = Daedalus;
        this.Identifier = Identifier;
    }
    
    public void dumplog(final Player player, final String log) {
        if (!this.DumpLogs.containsKey(player.getName())) {
            final List<String> logs = new ArrayList<String>();
            logs.add(log);
            this.DumpLogs.put(player.getName(), logs);
        }
        else {
            this.DumpLogs.get(player.getName()).add(log);
        }
    }
    
    public void onEnable() {
    }
    
    public void onDisable() {
    }
    
    public boolean isEnabled() {
        return this.Enabled;
    }
    
    public boolean isBannable() {
        return this.Bannable;
    }
    
    public boolean hasBanTimer() {
        return this.BanTimer;
    }
    
    public boolean isJudgmentDay() {
        return this.JudgementDay;
    }
    
    public Bernard getDaedalus() {
        return this.Daedalus;
    }
    
    public boolean hasDump(final Player player) {
        return this.DumpLogs.containsKey(player.getName());
    }
    
    public void clearDump(final Player player) {
        this.DumpLogs.remove(player.getName());
    }
    
    public void clearDumps() {
        this.DumpLogs.clear();
    }
    
    public Integer getMaxViolations() {
        return this.MaxViolations;
    }
    
    public Integer getViolationsToNotify() {
        return this.ViolationsToNotify;
    }
    
    public Long getViolationResetTime() {
        return this.ViolationResetTime;
    }
    
    public void setEnabled(final boolean Enabled) {
        if (this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".enabled") != Enabled && this.Daedalus.getConfig().get("checks." + this.getIdentifier() + ".enabled") != null) {
            this.Enabled = this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".enabled");
            return;
        }
        if (Enabled) {
            if (!this.isEnabled()) {
                this.Daedalus.RegisterListener((Listener)this);
            }
        }
        else if (this.isEnabled()) {
            HandlerList.unregisterAll((Listener)this);
        }
        this.Enabled = Enabled;
    }
    
    public void checkValues() {
        if (this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".enabled")) {
            this.setEnabled(true);
        }
        else {
            this.setEnabled(false);
        }
        if (this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".bannable")) {
            this.setBannable(true);
        }
        else {
            this.setEnabled(false);
        }
    }
    
    public void setBannable(final boolean Bannable) {
        if (this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".bannable") != Bannable && this.Daedalus.getConfig().get("checks." + this.getIdentifier() + ".bannable") != null) {
            this.Bannable = this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".bannable");
            return;
        }
        this.Bannable = Bannable;
    }
    
    public void setAutobanTimer(final boolean BanTimer) {
        if (this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".banTimer") != BanTimer && this.Daedalus.getConfig().get("checks." + this.getIdentifier() + ".banTimer") != null) {
            this.BanTimer = this.Daedalus.getConfig().getBoolean("checks." + this.getIdentifier() + ".banTimer");
            return;
        }
        this.BanTimer = BanTimer;
    }
    
    public void setMaxViolations(final int MaxViolations) {
        if (this.Daedalus.getConfig().getInt("checks." + this.getIdentifier() + ".maxViolations") != MaxViolations && this.Daedalus.getConfig().get("checks." + this.getIdentifier() + ".maxViolations") != null) {
            this.MaxViolations = this.Daedalus.getConfig().getInt("checks." + this.getIdentifier() + ".maxViolations");
            return;
        }
        this.MaxViolations = MaxViolations;
    }
    
    public void setViolationsToNotify(final int ViolationsToNotify) {
        this.ViolationsToNotify = ViolationsToNotify;
    }
    
    public void setViolationResetTime(final long ViolationResetTime) {
        this.ViolationResetTime = ViolationResetTime;
    }
    
    public void setJudgementDay(final boolean JudgementDay) {
        this.JudgementDay = JudgementDay;
    }
    
    public String getName() {
        return this.Name;
    }
    
    public String getIdentifier() {
        return this.Identifier;
    }
    
    public List<String> getDump(final Player player) {
        return this.DumpLogs.get(player.getName());
    }
    
    public String dump(final String player) {
        if (!this.DumpLogs.containsKey(player)) {
            return null;
        }
        final TxtFile file = new TxtFile(this.getDaedalus(), "/Dumps", String.valueOf(player) + "_" + this.getIdentifier());
        file.clear();
        for (final String Line : this.DumpLogs.get(player)) {
            file.addLine(Line);
        }
        file.write();
        return file.getName();
    }
}
