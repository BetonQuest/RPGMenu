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
import de.ungefroren.rpgmenu.config.SimpleYMLConfig;
import de.ungefroren.rpgmenu.utils.Log;
import de.ungefroren.rpgmenu.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
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

    private final ConfigPackage pack;
    private final int height;
    private final String title;
    private final HashMap<String, MenuItem> items;
    private final String[] slots;
    private final Optional<QuestItem> boundItem;
    private final List<ConditionID> openConditions;
    private final Optional<MenuBoundCommand> boundCommand;

    public Menu(MenuID id) throws InvalidConfigurationException {
        super(id.getFullID(), id.getFile());
        this.pack = id.getPackage();
        //load size
        this.height = getInt("height");
        if (this.height < 1 || this.height > 6) throw new Invalid("height");
        //load title
        this.title = Utils.translateAlternateColorcodes('&', getString("title"));
        //load opening conditions
        this.openConditions = new ArrayList<>();
        try {
            this.openConditions.addAll(getConditions("open_conditions", pack));
        } catch (Missing e) {
        }
        //load bound item
        this.boundItem = new OptionalSetting<QuestItem>() {
            @Override
            protected QuestItem of() throws Missing, Invalid {
                try {
                    return new QuestItem(new ItemID(pack, getString("bind")));
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
                if (!command.matches("[0-9A-Za-z\\-]+")) throw new Invalid("command");
                return new MenuBoundCommand(command);
            }
        }.get();
        // load items
        this.items = new HashMap<>();
        if (!config.isConfigurationSection("items")) throw new Missing("items");
        for (String key : config.getConfigurationSection("items").getKeys(false)) {
            try {
                items.put(key, new MenuItem(this.pack, key, config.getConfigurationSection("items.key")));
            } catch (InvalidSimpleConfigException e) {
                throw new InvalidSimpleConfigException(e);
            }
        }
        //load slots
        this.slots = new String[height * 9];
        if (!config.isConfigurationSection("slots")) throw new Missing("slots");
        for (String key : config.getConfigurationSection("slots").getKeys(false)) {
            if (!key.matches("\\d+")) throw new Invalid("slots", key + " is not a valid slot number");
            int slot = Integer.parseInt(key);
            if (slot > slots.length - 1) throw new Invalid("slots." + slot, "inventory only has " + (slots.length - 1) + "slots");
            String item = getString("slots." + key);
            if (!this.items.containsKey(item)) throw new Invalid("slots." + key, "item " + item + " not found");
            this.slots[slot] = item;
        }
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

    @EventHandler
    public void onItemClick(PlayerInteractEvent event) {
        if (event.isCancelled()) return;
        if (event.getAction() == Action.PHYSICAL) return;
        //check if item is bound item
        if (!boundItem.get().compare(event.getItem())) return;
        if (!mayOpen(event.getPlayer())) return;
        //TODO open Menu
    }

    private class MenuBoundCommand extends SimpleCommand {

        public MenuBoundCommand(String name) {
            super(name, 0);
        }

        @Override
        public List<String> simpleTabComplete(CommandSender sender, String alias, String[] args) {
            //TODO
            return null;
        }

        @Override
        public boolean simpleCommand(CommandSender sender, String alias, String[] args) {
            //TODO
            return false;
        }

        @Override
        protected String noPermissionMessage(CommandSender sender) {
            //TODO
            return null;
        }
    }
}
