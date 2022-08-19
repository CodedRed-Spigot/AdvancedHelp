package me.codedred.advancedhelp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import org.bukkit.plugin.java.JavaPlugin;


public class UpdateChecker {

    private int project = 44478;
    private URL checkURL;
    private String newVersion = "";
    private final JavaPlugin plugin;

    public UpdateChecker(JavaPlugin plugin, int projectID) {
        this.plugin = plugin;
        newVersion = plugin.getDescription().getVersion();
        project = projectID;
        try {
            checkURL = new URL(
                    "https://api.spigotmc.org/legacy/update.php?resource=" + projectID);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public int getProjectID() {
        return project;
    }

    public JavaPlugin getPlugin() {
        return plugin;
    }

    public String getLatestVersion() {
        return newVersion;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }

    public boolean checkForUpdates() throws Exception {
        URLConnection con = checkURL.openConnection();
        newVersion = new BufferedReader(new InputStreamReader(con.getInputStream()))
                .readLine();
        return !plugin.getDescription().getVersion().equals(newVersion);
    }
}
