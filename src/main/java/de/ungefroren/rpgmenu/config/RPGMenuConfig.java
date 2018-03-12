/**
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

package de.ungefroren.rpgmenu.config;

import de.ungefroren.rpgmenu.RPGMenu;
import de.ungefroren.rpgmenu.utils.Log;
import de.ungefroren.rpgmenu.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.database.PlayerData;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The plugins config file
 * <p>
 * Created on 13.01.2018
 *
 * @author Jonas Blocher
 */
public class RPGMenuConfig extends SimpleYMLConfig {

    public final boolean ingameUpdateNotifications;
    public final boolean debug;
    private HashMap<String, HashMap<String, String>> messages;
    private List<String> languages;

    public RPGMenuConfig() throws InvalidConfigurationException {
        super(new File(RPGMenu.getInstance().getDataFolder(), "config.yml"));
        //load languages
        if (!config.contains("messages") || !config.isConfigurationSection("messages"))
            throw new Missing("messages");
        this.messages = new HashMap<>();
        this.languages = new ArrayList<>();
        for (String key : config.getConfigurationSection("messages").getKeys(false)) {
            languages.add(key);
        }
        //load configuration settings
        this.ingameUpdateNotifications = getBoolean("ingameUpdateNotifications");
        this.debug = getBoolean("debug");

    }

    /**
     * Load a message from file into hash map
     *
     * @param key key of the message
     * @throws Missing if message isn't found in default language
     */
    private void loadMessage(String key) throws Missing {
        for (String lang : this.languages) {
            try {
                HashMap<String, String> msgs = messages.get(lang);
                if (msgs == null) msgs = new HashMap<>();
                msgs.put(key, Utils.translateAlternateColorcodes('&', getString("messages." + lang + "." + key)));
                this.messages.put(lang, msgs);
            } catch (Missing e) {
                if (lang.equals(Config.getLanguage())) throw e;
            }
        }
    }

    /**
     * Get a message in a specific language by it's key
     *
     * @param lang    language of the message
     * @param key     key of the message
     * @param replace arguments in the message that should be replaced
     * @return the predefined message with all args replaced
     */
    public String getMessage(String lang, String key, String... replace) {
        String message;
        if (lang == null) lang = Config.getLanguage();
        try {
            message = messages.get(lang).get(key);
            if (message == null) throw new NullPointerException();
        } catch (NullPointerException e) {
            try {
                message = messages.get(Config.getLanguage()).get(key);
                if (message == null) throw new NullPointerException();
            } catch (NullPointerException ex) {
                return "null";
            }
        }
        if (replace != null) {
            for (int i = 0; i < replace.length; i++) {
                message = message.replace("{" + i + "}", replace[i]);
            }
        }
        return message;
    }

    /**
     * Sends the player a predefined message
     *
     * @param player  the recipient of the message
     * @param key     the key of the message
     * @param replace arguments in the message that should be replaced
     */
    public void sendMessage(Player player, String key, String... replace) {
        PlayerData playersData = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID(player));
        player.sendMessage(this.getMessage(playersData.getLanguage(), key, replace));
    }

    /**
     * Sends a predefined message to a command sender
     *
     * @param sender  the recipient of the message
     * @param key     the key of the message
     * @param replace arguments in the message that should be replaced
     */
    public void sendMessage(CommandSender sender, String key, String... replace) {
        if (sender instanceof Player) {
            this.sendMessage((Player) sender, key, replace);
        } else {
            Log.info(this.getMessage(null, key, replace));
        }
    }

}
