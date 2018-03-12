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

import de.ungefroren.rpgmenu.RPGMenu;
import de.ungefroren.rpgmenu.utils.Log;
import de.ungefroren.rpgmenu.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Abstract class to simplify creation of commands and implementation of tab complete
 * <p>
 * Created on 06.09.2017
 *
 * @author Jonas Blocher
 */
public abstract class SimpleCommand extends Command implements PluginIdentifiableCommand {

    public final int minimalArgs;
    private Permission perimssion;
    private CommandMap commandMap = null;

    public SimpleCommand(String name, int minimalArgs) {
        super(name);
        this.minimalArgs = minimalArgs;
        this.perimssion = null;
    }

    public SimpleCommand(String name, Permission reqPermission, int minimalArgs) {
        this(name, minimalArgs);
        this.perimssion = reqPermission;
    }

    /**
     * Override this method to handle what is returned on tab complete
     *
     * @param sender the CommandSender performing the tab complete
     * @param alias  the command alias used
     * @param args   the arguments specified
     * @return must be a list of all possible competitions for the current arg, ignoring already typed chars
     */
    public abstract List<String> simpleTabComplete(CommandSender sender, String alias, String[] args);

    /**
     * Override this method to handle what happens if the command gets executed, all permissions are met and required arguments are
     * given
     *
     * @param sender the CommandSender performing the command
     * @param alias  the command alias used
     * @param args   the arguments specified
     * @return whether the command could be successfully executed or not
     */
    public abstract boolean simpleCommand(CommandSender sender, String alias, String[] args);

    /**
     * Override this method to specify the message which is send when the command sender doesn't has the required Permission
     *
     * @param sender the CommandSender performing the command
     * @return the message to send
     */
    protected abstract String noPermissionMessage(CommandSender sender);

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (args.length < minimalArgs) {
            sender.sendMessage("§cUsage:\n§7" + super.getUsage());
            return false;
        }
        if (perimssion != null) {
            if (!sender.hasPermission(perimssion)) {
                sender.sendMessage(noPermissionMessage(sender));
                return false;
            }
        }
        return simpleCommand(sender, label, args);
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        if (sender == null || alias == null || args == null) return super.tabComplete(sender, alias, args);
        List<String> completations = this.simpleTabComplete(sender, alias, args);
        if (completations == null) return null;
        List<String> out = new ArrayList<>();
        String lastArg = args[args.length - 1];
        for (String completation : completations) {
            if (lastArg == null || lastArg.matches(" *") || completation.startsWith(lastArg)) {
                out.add(completation);
            }
        }
        return out;
    }

    @Override
    public List<String> tabComplete(CommandSender sender,
                                    String alias,
                                    String[] args,
                                    Location location) throws IllegalArgumentException {
        return this.tabComplete(sender, alias, args);
    }

    /**
     * Method to register the command
     */
    public boolean register() {
        try {
            PluginManager manager = Bukkit.getPluginManager();
            Class<? extends PluginManager> managerClass = manager.getClass();
            this.commandMap = (CommandMap) Utils.getField(managerClass, "commandMap").get(manager);
            this.register(commandMap);
            return true;
        } catch (Exception e) {
            Log.error("Could not register command " + getName() + ":");
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Method to unregister the command
     */
    @SuppressWarnings("unchecked")
    public boolean unregsiter() {
        if (this.commandMap == null) return false;
        try {
            this.unregister(commandMap);
            Collection<Command> commands = (Collection<Command>) Utils
                    .getMethod(commandMap.getClass(), "getCommands", 0)
                    .invoke(commandMap);
            if (commands.getClass().getSimpleName().equals("UnmodifiableCollection")) {
                Field originalField = commands.getClass().getDeclaredField("c");
                originalField.setAccessible(true);
                Collection<Command> original = (Collection<Command>) originalField.get(commands);
                original.remove(this);
            } else {
                commands.remove(this);
            }
            return true;
        } catch (Exception e) {
            Log.error("Could not unregister command " + getName() + ":");
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Plugin getPlugin() {
        return RPGMenu.getInstance();
    }
}
