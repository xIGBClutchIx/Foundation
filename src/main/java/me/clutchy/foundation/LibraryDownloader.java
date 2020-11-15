package me.clutchy.foundation;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LibraryDownloader {

    private static final ArrayList<String> loadedArtifacts = new ArrayList<>();
    private final Gson gson = new GsonBuilder().create();

    private final ClassLoader classLoader;
    private final Logger logger;

    public LibraryDownloader(ClassLoader classLoader, Logger logger) {
        this.classLoader = classLoader;
        this.logger = logger;
    }

    public void downloadLibraries(JavaPlugin plugin, InputStream stream) {
        List<Library> libraries = new ArrayList<>();
        // Read from our json file we gave it.
        try (Reader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            Type listType = new TypeToken<ArrayList<Library>>(){}.getType();
            libraries = gson.fromJson(reader, listType);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Error reading libraries json", e);
            System.exit(0);
        }
        downloadLibraries(plugin, libraries);
    }

    public void downloadLibraries(JavaPlugin plugin, List<Library> libraries) {
        plugin.getLogger().info("Loading libraries");
        List<String> loadedLibrariesIds = new ArrayList<>();
        // Sort all libraries
        Collections.sort(libraries);
        // Lock to libraries so we don't continue until we are out of libraries
        CountDownLatch latch = new CountDownLatch(libraries.size());
        for (Library library : libraries) {
            new Thread(() -> {
                // Make sure we don't have this artifact loaded.
                if (loadedArtifacts.contains(library.groupId + ":" + library.artifactId)) return;
                File libraryPath = Paths.get("cache", getPath(library)).toFile();
                try {
                    // Create default directories
                    Files.createDirectories(libraryPath.toPath());
                    File jar = new File(libraryPath, getFileName(library, false));
                    // If the file does not exist then try to download it.
                    // If it does then check to make sure it is valid based off MD5.
                    if (!jar.exists()) {
                        downloadFile(plugin, library, jar, false);
                    } else {
                        plugin.getLogger().info("Checking library: " + ChatColor.YELLOW + library.artifactId);
                        // Read the libraries MD5
                        String fileMd5 = toHexString(MessageDigest.getInstance("MD5").digest(Files.readAllBytes(jar.toPath())));
                        // Make sure our file MD5 is still valid?
                        if (!fileMd5.trim().isEmpty()) {
                            // Read the library md5 from url
                            try (BufferedReader readerUrl = new BufferedReader(new InputStreamReader(getConnection(library, true)))) {
                                String urlMd5 = readerUrl.readLine();
                                if (urlMd5 != null && !urlMd5.equalsIgnoreCase(fileMd5)) {
                                    downloadFile(plugin, library, jar, true);
                                }
                            } catch (Exception ignored) {
                                // We don't have a url for the md5 so just continue on sadly.
                            }
                        }
                    }
                    // Add to the class loader - Spigot use a url class loader.
                    LoadJarsUtil.addFile(classLoader, logger, jar);
                    // Add to global artifacts so we don't get duplicates. Possibly do version checking in the future and unload?
                    loadedArtifacts.add(library.groupId + ":" + library.artifactId);
                    // Add to local list of libraries
                    loadedLibrariesIds.add(library.artifactId);
                } catch (Exception e) {
                    // We encountered a error with needed libraries so we can't proceed, log and shutdown.
                    plugin.getLogger().log(Level.SEVERE, "Error loading library: " + library.artifactId, e);
                    System.exit(0);
                } finally {
                    latch.countDown();
                }
            }).start();
        }
        try {
            latch.await();
        } catch (InterruptedException e) {
            plugin.getLogger().severe("Error loading libraries");
            System.exit(0);
        }
        // Log all our libraries
        if (!loadedLibrariesIds.isEmpty()) {
            Collections.sort(loadedLibrariesIds);
            String loadedLibs = loadedLibrariesIds.toString().substring(1).replaceFirst("]", "").replace(", ", ChatColor.RESET + ", " + ChatColor.DARK_PURPLE);
            plugin.getLogger().info("Loaded libraries: " + ChatColor.DARK_PURPLE + loadedLibs);
        }
    }

    // Turns MD5 bytes to hex string needed for libraries.
    private String toHexString(byte[] bytes) {
        StringBuilder hexString = new StringBuilder();
        for (byte aByte : bytes) {
            String hex = Integer.toHexString(0xFF & aByte);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    private URL getUrl(Library library, boolean MD5) throws MalformedURLException {
        if (library.repo == null || library.repo.trim().isEmpty()) library.repo = "https://jcenter.bintray.com/";
        if (!library.repo.endsWith("/")) library.repo += "/";
        return new URL(library.repo + getPath(library) + getFileName(library, MD5));
    }

    private String getFileName(Library library, boolean MD5) {
        return library.artifactId + "-" + library.version + ".jar" + (MD5 ? ".md5" : "");
    }

    private String getPath(Library library) {
        return library.groupId.replaceAll("\\.", "/") + "/" + library.artifactId + "/" + library.version + "/";
    }

    private void downloadFile(JavaPlugin plugin, Library library, File location, boolean reDownload) throws IOException {
        plugin.getLogger().info((reDownload ? ChatColor.DARK_RED + "Red" : "D") + "ownloading library" + ChatColor.RESET + ": " + ChatColor.DARK_PURPLE + library.artifactId);
        // Download and copy file. Catch error here in the future?
        try (InputStream inputStream = getConnection(library, false)) {
            Files.copy(inputStream, location.toPath(), StandardCopyOption.REPLACE_EXISTING);
        }
    }

    // Default connection process for libraries with user-agent to make sure nothing goes wrong.
    private InputStream getConnection(Library library, boolean MD5) throws IOException {
        HttpURLConnection connection = (HttpURLConnection) getUrl(library, MD5).openConnection();
        connection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
        connection.setRequestMethod("GET");
        connection.connect();
        return connection.getInputStream();
    }

    public class Library implements Comparable<Library> {
        private final String groupId;
        private final String artifactId;
        private final String version;
        private String repo;

        public Library(String groupId, String artifactId, String version, String repo) {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.version = version;
            this.repo = repo;
        }

        @Override
        public int compareTo(@NotNull Library other) {
            return artifactId.compareTo(other.artifactId);
        }
    }
}
