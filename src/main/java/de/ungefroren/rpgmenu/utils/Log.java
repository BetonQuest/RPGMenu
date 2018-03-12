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

package de.ungefroren.rpgmenu.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

/**
 * Utility class for simpler logging and debugging
 * <p>
 * Created on 13.01.2018
 *
 * @author Jonas Blocher
 */
public class Log {

    private static ConsoleCommandSender console = Bukkit.getConsoleSender();
    private static boolean debug = false;

    /**
     * Set whether debug messages should be displayed
     *
     * @param debug true if debug mode is enabled
     */
    public static void setDebug(boolean debug) {
        Log.debug = debug;
    }

    /**
     * Log a message with log level <b>INFO</b>
     *
     * @param message
     */
    public static void info(String message) {
        console.sendMessage("[RPGMenu] " + message);
    }


    /**
     * Log a debug message with log level <b>INFO</b> if debugging is enabled
     *
     * @param message
     */
    public static void debug(String message) {
        if (!debug) return;
        console.sendMessage("[RPGMenu][DEBUG] " + message);
    }

    /**
     * Log a debug message with log level <b>INFO</b> if debugging is enabled
     *
     * @param object to show in debug message
     */
    public static void debug(Object object) {
        debug(String.valueOf(object));
    }

    /**
     * Log a message with log level <b>ERROR</b>
     *
     * @param message
     */
    public static void error(String message) {
        console.sendMessage(ChatColor.DARK_RED + "[RPGMenu] " + message);
    }

    /**
     * Log the error message of a throwable with log level <b>ERROR</b>
     *
     * @param throwable
     */
    public static void error(Throwable throwable) {
        error(throwable.getMessage());
    }

}
