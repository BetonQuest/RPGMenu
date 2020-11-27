/*
 * RPGMenu
 * <p>
 * Copyright (C) 2018 Jonas Blocher
 * <p>
 * Licensed under:
 * DON'T BE A DICK PUBLIC LICENSE
 * Version 1.1, December 2016
 * <p>
 * see: https://github.com/joblo2213/RPGMenu/blob/master/LICENSE
 */

package de.ungefroren.rpgmenu.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.utils.Updater.UpdateStrategy;
import pl.betoncraft.betonquest.utils.Updater.Version;

import de.ungefroren.rpgmenu.RPGMenu;

/**
 * Utility class that informs about available updates and keeps the config up to date
 * <p>
 * Created on 12.01.2018
 *
 * @author Jonas Blocher
 */
public class Updater {

    private static final String VERSION_TEXT_URL = "https://raw.githubusercontent.com/BetonQuest/RPGMenu/master/version.txt";
    private static final String LATEST_DOWNLOAD_URL = "https://github.com/BetonQuest/RPGMenu/releases";
    private Version current;
    private Version latest;
    private boolean error = false;
    private UpdateNotifier updateNotifier = null;


    public Updater() {
        try {
            this.current = new Version(RPGMenu.getInstance().getDescription().getVersion());
            BufferedReader br = new BufferedReader(new InputStreamReader(new URL(VERSION_TEXT_URL).openStream()));
            String line = br.readLine();
            br.close();
            try {
                this.latest = new Version(line.trim());
            } catch (IllegalArgumentException | NullPointerException e) {
                throw new IOException("version.txt is invalid", e);
            }
        } catch (IOException e) {
            Log.error("Error while initialising updater: §c" + e.getMessage());
            this.error = true;
        }
    }

    /**
     * @return if the current version is up to date (if updater is not initialised this returns true)
     */
    public boolean isVersionUpToDate() {
        return error || !current.isNewer(latest, UpdateStrategy.MAYOR);
    }

    /**
     * Updates the plugins config after switching to a new version to keep it working
     * (if updater is not initialised this does nothing)
     */
    public void updateConfig() {
        if (error) return;
        FileConfiguration defaultConfig = YamlConfiguration.loadConfiguration(
                new InputStreamReader(RPGMenu.getInstance().getResource("rpgmenu.config.yml")));
        FileConfiguration currentConfig = YamlConfiguration.loadConfiguration(
                new File(BetonQuest.getInstance().getDataFolder(), "rpgmenu.config.yml"));
        //check config version
        if (defaultConfig.getInt("config-version") == currentConfig.getInt("config-version")) return;
        //update
        Log.info("RPGMenuConfig is not up to date. Starting update....");
        for (String key : defaultConfig.getKeys(true)) {
            Object value = defaultConfig.get(key);
            if (!currentConfig.contains(key)) {
                currentConfig.set(key, value);
                Log.info("Added setting '" + key + "' to config");
            }
        }
        currentConfig.set("config-version", defaultConfig.getInt("config-version"));
        try {
            File config = new File(BetonQuest.getInstance().getDataFolder(), "rpgmenu.config.yml");
            File old = new File(BetonQuest.getInstance().getDataFolder(), "rpgmenu.config_old.yml");
            //keep a copy of the old rpgmenu.config.yml
            Files.move(config.toPath(), old.toPath(), StandardCopyOption.REPLACE_EXISTING);
            currentConfig.save(config);
            Log.info("Updated config to version " + defaultConfig.getInt("config-version") + "!");
        } catch (IOException e) {
            Log.error("An error occurred while updating the config: " + e.getMessage());
        }
    }

    /**
     * Notify about new versions and the risks of dev versions
     * (if updater is not initialised this does nothing)
     */
    public void showVersionInfo() {
        if (error) return;
        if (current.isDev()) {
            Log.info("You are using an development build of RPGMenu.\n" +
                    "Development builds may contain bugs so be cautious.\n" +
                    "If you find some please report them on the plugins GitHub page so I can fix them.");
        }
        if (isVersionUpToDate()) return;
        Log.error("You are using an outdated version of RPGMenu. Please update to " + latest);
    }

    /**
     * Enables the in-game update notifier if a old plugin version is used and config setting is true
     */
    public void enableUpdateNotifier() {
        if (isVersionUpToDate()) return;
        if (!RPGMenu.getConfiguration().ingameUpdateNotifications) return;
        this.updateNotifier = new UpdateNotifier();
        Bukkit.getPluginManager().registerEvents(this.updateNotifier, RPGMenu.getInstance());
    }

    /**
     * Disables the in-game update notifier
     */
    public void disableUpdateNotifier() {
        if (this.updateNotifier == null) return;
        HandlerList.unregisterAll(this.updateNotifier);
        this.updateNotifier = null;
    }

    /**
     * Notifies all players with OP on join about new plugin versions
     */
    private class UpdateNotifier implements Listener {

        @EventHandler
        public void notifyOps(PlayerJoinEvent event) {
            Player player = event.getPlayer();
            if (!player.isOp()) return;
            ComponentBuilder builder = new ComponentBuilder("");
            builder
                    .append(TextComponent.fromLegacyText("§7[§c§li§7] §cThere is a new version of §6RPGMenu§c available: "))
                    .append(latest.toString()).color(ChatColor.GRAY).underlined(true)
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, TextComponent.fromLegacyText("§9§n" + LATEST_DOWNLOAD_URL)))
                    .event(new ClickEvent(ClickEvent.Action.OPEN_URL, LATEST_DOWNLOAD_URL));
            player.spigot().sendMessage(builder.create());
        }
    }
}
