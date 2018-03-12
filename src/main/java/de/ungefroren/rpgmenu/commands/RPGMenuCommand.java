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

package de.ungefroren.rpgmenu.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * The plugins main command
 *
 * Created on 14.01.2018
 *
 * @author Jonas Blocher
 */
public class RPGMenuCommand extends SimpleCommand {

    public RPGMenuCommand(String name, int minimalArgs) {
        super(name, minimalArgs);
    }

    @Override
    public List<String> simpleTabComplete(CommandSender sender, String alias, String[] args) {
        return null;
    }

    @Override
    public boolean simpleCommand(CommandSender sender, String alias, String[] args) {
        return false;
    }


    @Override
    protected String noPermissionMessage(CommandSender sender) {
        return "";
    }
}
