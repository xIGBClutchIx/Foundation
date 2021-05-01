package me.clutchy.foundation;

import org.bukkit.plugin.java.JavaPlugin;

public class FoundationStartup extends JavaPlugin {

    private final LibraryDownloader libraryDownloader = new LibraryDownloader(getClassLoader(), getLogger());
    private Foundation foundation;

    @Override
    public void onEnable() {
        libraryDownloader.downloadLibraries(this, getClassLoader().getResourceAsStream("libraries.json"));
        foundation = new Foundation(this);
        foundation.onEnable();
    }

    @Override
    public void onDisable() {
        foundation.onDisable();
    }
}
