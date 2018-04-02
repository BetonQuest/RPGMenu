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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import de.ungefroren.rpgmenu.RPGMenu;
import de.ungefroren.rpgmenu.utils.Utils;

/**
 * The plugins config file
 * <p>
 * Created on 13.01.2018
 *
 * @author Jonas Blocher
 */
public class RPGMenuConfig extends SimpleYMLConfig {

    /**
     * If update notifications should be displayed ingame
     */
    public final boolean ingameUpdateNotifications;

    /**
     * If debugging is turned on
     */
    public final boolean debug;

    /**
     * Default value if menus close when a item was clicked
     */
    public final boolean defaultCloseOnClick;

    /**
     * Hashmap containing all messages for each language
     */
    private HashMap<String, HashMap<String, String>> messages;

    /**
     * List containing all languages
     */
    private List<String> languages;

    public RPGMenuConfig() throws InvalidConfigurationException {
        super(new File(BetonQuest.getInstance().getDataFolder(), "rpgmenu.config.yml"));
        //load languages
        if (!config.contains("messages") || !config.isConfigurationSection("messages"))
            throw new Missing("messages");
        this.messages = new HashMap<>();
        this.languages = new ArrayList<>();
        for (String key : config.getConfigurationSection("messages").getKeys(false)) {
            languages.add(key);
        }
        //load configuration settings
        this.ingameUpdateNotifications = getBoolean("ingame_update_notifications");
        this.debug = getBoolean("debug");
        this.defaultCloseOnClick = getBoolean("default_close");

        //load all messages
        this.loadMessage("command_no_permission");
        this.loadMessage("menu_do_not_open");
        this.loadMessage("command_usage");
        this.loadMessage("command_info_reload");
        this.loadMessage("command_info_list");
        this.loadMessage("command_info_open");
        this.loadMessage("command_invalid_menu");
        this.loadMessage("command_invalid_player");
        this.loadMessage("command_no_player");
        this.loadMessage("command_no_menu");
        this.loadMessage("command_open_successful");
        this.loadMessage("command_reload_successful");
        this.loadMessage("command_reload_failed");
        this.loadMessage("command_list");
        this.loadMessage("click_to_open");
    }

    /**
     * Get a message in a specific language by it's key
     *
     * @param lang    language of the message
     * @param key     key of the message
     * @param replace arguments in the message that should be replaced
     * @return the predefined message with all args replaced
     */
    public static String getMessage(String lang, String key, String... replace) {
        RPGMenuConfig instance = RPGMenu.getConfiguration();
        if (instance == null) return "null";
        String message;
        if (lang == null) lang = Config.getLanguage();
        try {
            message = instance.messages.get(lang).get(key);
            if (message == null) throw new NullPointerException();
        } catch (NullPointerException e) {
            try {
                message = instance.messages.get(Config.getLanguage()).get(key);
                if (message == null) throw new NullPointerException();
            } catch (NullPointerException ex) {
                return "null";
            }
        }
        if (replace != null) {
            for (int i = 1; i <= replace.length; i++) {
                message = message.replace("{" + i + "}", replace[i - 1]);
            }
        }
        return message;
    }

    /**
     * Get a translated message for a command sender
     *
     * @param sender  who the message should be displayed to
     * @param key     key of the message
     * @param replace arguments in the message that should be replaced
     * @return the predefined message with all args replaced
     */
    public static String getMessage(CommandSender sender, String key, String... replace) {
        String lang = null;
        if (sender instanceof Player) {
            lang = BetonQuest.getInstance().getPlayerData(PlayerConverter.getID((Player) sender)).getLanguage();
        }
        return RPGMenuConfig.getMessage(lang, key, replace);
    }

    /**
     * Sends a predefined message to a command sender
     *
     * @param sender  the recipient of the message
     * @param key     the key of the message
     * @param replace arguments in the message that should be replaced
     */
    public static void sendMessage(CommandSender sender, String key, String... replace) {
        sender.sendMessage(RPGMenuConfig.getMessage(sender, key, replace));
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
                msgs.put(key, Utils.translateAlternateColorcodes('&', getString("messages." + lang + "." + key)).replace("\\n", "\n"));
                this.messages.put(lang, msgs);
            } catch (Missing e) {
                if (lang.equals(Config.getLanguage())) throw e;
            }
        }
    }

}
