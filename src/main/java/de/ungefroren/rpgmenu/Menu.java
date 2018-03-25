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

package de.ungefroren.rpgmenu;


import de.ungefroren.rpgmenu.commands.SimpleCommand;
import de.ungefroren.rpgmenu.config.RPGMenuConfig;
import de.ungefroren.rpgmenu.config.SimpleYMLConfig;
import de.ungefroren.rpgmenu.utils.Log;
import de.ungefroren.rpgmenu.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import pl.betoncraft.betonquest.*;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.item.QuestItem;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;


/**
 * Class representing a menu
 * <p>
 * Created on 11.03.2018
 *
 * @author Jonas Blocher
 */
public class Menu extends SimpleYMLConfig implements Listener {

    /**
     * The internal id of the menu
     */
    private final MenuID ID;

    /**
     * The height of the menu in slots
     */
    private final int height;

    /**
     * The title of the menu
     */
    private final String title;

    /*
     * Hashmap with a items id as key and the menu item object containing all data of the item
     *
     * Currently this information isn't needed after initialising the Menu
     */
    //private final HashMap<String, MenuItem> itemsMap;

    /**
     * List of all slots objects as defined in the slots section
     */
    private final List<Slots> slots;

    /**
     * Optional which contains the item this menu is bound to or is empty if none is bound
     */
    private final Optional<QuestItem> boundItem;

    /**
     * Conditions which have to be matched to open the menu
     */
    private final List<ConditionID> openConditions;

    /**
     * Events which are fired when the menu is opened
     */
    private final List<EventID> openEvents;

    /**
     * Events which are fired when the menu is closed
     */
    private final List<EventID> closeEvents;

    /**
     * Optional which contains the command this menu is bound to or is empty if none is bound
     */
    private final Optional<MenuBoundCommand> boundCommand;

    public Menu(MenuID id) throws InvalidConfigurationException {
        super(id.getFullID(), id.getFile());
        this.ID = id;
        //load size
        this.height = getInt("height");
        if (this.height < 1 || this.height > 6) throw new Invalid("height");
        //load title
        this.title = Utils.translateAlternateColorcodes('&', getString("title"));
        //load opening conditions
        this.openConditions = new ArrayList<>();
        try {
            this.openConditions.addAll(getConditions("open_conditions", this.ID.getPackage()));
        } catch (Missing e) {
        }
        //load opening events
        this.openEvents = new ArrayList<>();
        try {
            this.openEvents.addAll(getEvents("open_events", this.ID.getPackage()));
        } catch (Missing e) {
        }
        //load closing events
        this.closeEvents = new ArrayList<>();
        try {
            this.closeEvents.addAll(getEvents("close_events", this.ID.getPackage()));
        } catch (Missing e) {
        }
        //load bound item
        this.boundItem = new OptionalSetting<QuestItem>() {
            @Override
            protected QuestItem of() throws Missing, Invalid {
                try {
                    return new QuestItem(new ItemID(ID.getPackage(), getString("bind")));
                } catch (ObjectNotFoundException | InstructionParseException e) {
                    throw new Invalid("bind", e);
                }
            }
        }.get();
        //load bound command
        this.boundCommand = new OptionalSetting<MenuBoundCommand>() {
            @Override
            protected MenuBoundCommand of() throws Missing, Invalid {
                String command = getString("command").trim();
                if (!command.matches("/*[0-9A-Za-z\\-]+")) throw new Invalid("command");
                if (command.startsWith("/")) command = command.substring(1);
                return new MenuBoundCommand(command);
            }
        }.get();
        // load items
        HashMap<String, MenuItem> itemsMap = new HashMap<>();
        if (!config.isConfigurationSection("items")) throw new Missing("items");
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            try {
                itemsMap.put(key, new MenuItem(this.ID.getPackage(), key, config.getConfigurationSection("items." + key)));
            } catch (InvalidSimpleConfigException e) {
                throw new InvalidSimpleConfigException(e);
            }
        }
        //load slots
        this.slots = new ArrayList<>();
        if (!config.isConfigurationSection("slots")) throw new Missing("slots");
        for (String key : config.getConfigurationSection("slots").getKeys(false)) {
            List<MenuItem> itemsList = new ArrayList<>();
            //check if items from list are all valid
            for (String item : getStrings("slots." + key)) {
                if (!itemsMap.containsKey(item))
                    throw new Invalid("slots." + key, "item " + item + " not found");
                else itemsList.add(itemsMap.get(item));
            }
            // create a new slots object and add it to list
            try {
                this.slots.add(new Slots(key, itemsList));
            } catch (IllegalArgumentException e) {
                throw new Invalid("slots", e);
            }
        }
        //check for doubled assigned slots
        try {
            Slots.checkSlots(this.slots);
        } catch (Slots.SlotDoubledException e) {
            throw new Invalid("slots." + e.getSlots(), e);
        }

        //load command and register listener
        this.boundCommand.ifPresent(SimpleCommand::register);
        if (this.boundItem.isPresent()) Bukkit.getPluginManager().registerEvents(this, RPGMenu.getInstance());
    }

    /**
     * Checks whether a player may open this menu
     *
     * @param player the player to check
     * @return true if all opening conditions are true, false otherwise
     */
    public boolean mayOpen(Player player) {
        String playerId = PlayerConverter.getID(player);
        for (ConditionID conditionID : openConditions) {
            if (!BetonQuest.condition(playerId, conditionID)) {
                Log.debug("Denied opening of " + name + ": Condition " + conditionID + "returned false.");
                return false;
            }
        }
        return true;
    }

    /**
     * Unregisters listeners and commands for this menu
     * <p>
     * Run this method on reload
     */
    public void unregister() {
        boundCommand.ifPresent(SimpleCommand::unregsiter);
        if (boundItem.isPresent()) HandlerList.unregisterAll(this);
    }

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getAction() == Action.PHYSICAL) return;
        //check if item is bound item
        if (!boundItem.get().compare(event.getItem())) return;
        event.setCancelled(true);
        if (!mayOpen(event.getPlayer())) {
            RPGMenuConfig.sendMessage(event.getPlayer(), "menu_do_not_open");
            return;
        }
        //open the menu
        Log.debug(event.getPlayer().getName() + " used bound item of menu " + this.ID);
        RPGMenu.openMenu(event.getPlayer(), this.ID);
    }

    /**
     * Runs all open events for the specified player
     *
     * @param player the player to run the events for
     */
    public void runOpenEvents(Player player) {
        Log.debug("Menu " + ID + ": Running open events");
        for (EventID event : this.openEvents) {
            BetonQuest.event(PlayerConverter.getID(player), event);
            Log.debug("Menu " + ID + ": Run event " + event);
        }
    }

    /**
     * Runs all close events for the specified player
     *
     * @param player the player to run the events for
     */
    public void runCloseEvents(Player player) {
        Log.debug("Menu " + ID + ": Running close events");
        for (EventID event : this.closeEvents) {
            BetonQuest.event(PlayerConverter.getID(player), event);
            Log.debug("Menu " + ID + ": Run event " + event);
        }
    }

    /**
     * @return the menu id of this menu
     */
    public MenuID getID() {
        return ID;
    }

    /**
     * @return the package this menu is located in
     */
    public ConfigPackage getPackage() {
        return this.ID.getPackage();
    }

    /**
     * @return the height of the menu in slots
     */
    public int getHeight() {
        return height;
    }

    /**
     * @return the title of the menu
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param player the player to get the items for
     * @return get the items for all slots
     */
    public MenuItem[] getItems(Player player) {
        MenuItem[] items = new MenuItem[this.height * 9];
        for (int i = 0; i < items.length; i++) {
            items[i] = this.getItem(player, i);
        }
        return items;
    }

    /**
     * Get a menu item for a specific slot
     *
     * @param player the player to get the item for
     * @param slot   for which the item should be get
     * @return menu item for that slot or null if none is specified
     */
    public MenuItem getItem(Player player, int slot) {
        for (Slots slots : this.slots) {
            if (slots.containsSlot(slot)) {
                return slots.getItem(player, slot);
            }
        }
        return null;
    }

    /**
     * @return the item this inventory is bound to
     */
    public Optional<QuestItem> getBoundItem() {
        return boundItem;
    }

    /**
     * @return a list containing all conditions which have to be met to open the menu
     */
    public List<ConditionID> getOpenConditions() {
        return openConditions;
    }

    /**
     * @return the command this inventory is bound to
     */
    public Optional<MenuBoundCommand> getBoundCommand() {
        return boundCommand;
    }

    /**
     * A command which can be used to open the gui
     * To perform the command a player must match all open conditions
     */
    private class MenuBoundCommand extends SimpleCommand {

        public MenuBoundCommand(String name) {
            super(name, 0);
        }

        @Override
        public boolean simpleCommand(CommandSender sender, String alias, String[] args) {
            if (!(sender instanceof Player)) {
                sender.sendMessage("§cCommand can only be run by players!");
                return false;
            }
            Player player = (Player) sender;
            if (mayOpen(player)) {
                Log.debug(player.getName() + " run bound command of " + ID);
                RPGMenu.openMenu(player, ID);
                return true;
            } else {
                player.sendMessage(this.noPermissionMessage(sender));
                return false;
            }
        }

        @Override
        protected String noPermissionMessage(CommandSender sender) {
            return RPGMenuConfig.getMessage(sender, "menu_do_not_open");
        }
    }
}
