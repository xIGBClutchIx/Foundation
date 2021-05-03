package me.clutchy.foundation;

import me.clutchy.dependenciesgen.downloader.DependencyDownloader;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class FoundationStartup extends JavaPlugin {

    private final DependencyDownloader dependencyDownloader = new DependencyDownloader(getClassLoader(), getLogger());
    private Foundation foundation;

    @Override
    public void onEnable() {
        dependencyDownloader.downloadDependencies(getClassLoader().getResourceAsStream("META-INF" + File.separator + "dependencies.json"));
        foundation = new Foundation(this);
        foundation.onEnable();
    }

    @Override
    public void onDisable() {
        foundation.onDisable();
    }
}
