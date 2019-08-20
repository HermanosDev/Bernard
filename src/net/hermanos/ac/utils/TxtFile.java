package net.hermanos.ac.utils;

import org.bukkit.plugin.java.*;
import java.util.*;
import java.io.*;

public class TxtFile
{
    private File File;
    private String Name;
    private List<String> Lines;
    
    public TxtFile(final JavaPlugin Plugin, final String Path, final String Name) {
        this.Lines = new ArrayList<String>();
        (this.File = new File(Plugin.getDataFolder() + Path)).mkdirs();
        this.File = new File(Plugin.getDataFolder() + Path, String.valueOf(Name) + ".txt");
        try {
            this.File.createNewFile();
        }
        catch (IOException ex) {}
        this.Name = Name;
        this.readTxtFile();
    }
    
    public void clear() {
        this.Lines.clear();
    }
    
    public void addLine(final String line) {
        this.Lines.add(line);
    }
    
    public void write() {
        try {
            final FileWriter fw = new FileWriter(this.File, false);
            final BufferedWriter bw = new BufferedWriter(fw);
            for (final String Line : this.Lines) {
                bw.write(Line);
                bw.newLine();
            }
            bw.close();
            fw.close();
        }
        catch (Exception ex) {}
    }
    
    public void readTxtFile() {
        this.Lines.clear();
        try {
            final FileReader fr = new FileReader(this.File);
            final BufferedReader br = new BufferedReader(fr);
            String Line;
            while ((Line = br.readLine()) != null) {
                this.Lines.add(Line);
            }
            br.close();
            fr.close();
        }
        catch (Exception exx) {
            exx.printStackTrace();
        }
    }
    
    public String getName() {
        return this.Name;
    }
    
    public String getText() {
        String text = "";
        for (int i = 0; i < this.Lines.size(); ++i) {
            final String line = this.Lines.get(i);
            text = String.valueOf(text) + line + ((this.Lines.size() - 1 == i) ? "" : "\n");
        }
        return text;
    }
    
    public List<String> getLines() {
        return this.Lines;
    }
}
