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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.ConditionID;
import pl.betoncraft.betonquest.EventID;
import pl.betoncraft.betonquest.ItemID;
import pl.betoncraft.betonquest.VariableNumber;
import pl.betoncraft.betonquest.config.Config;
import pl.betoncraft.betonquest.config.ConfigPackage;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.ObjectNotFoundException;
import pl.betoncraft.betonquest.exceptions.QuestRuntimeException;
import pl.betoncraft.betonquest.item.QuestItem;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import de.ungefroren.rpgmenu.config.SimpleYMLSection;
import de.ungefroren.rpgmenu.utils.Log;

/**
 * A Item which Is displayed as option in a menu and has some events that are fired when item is clicked
 * <p>
 * Created on 24.01.2018
 *
 * @author Jonas Blocher
 */
public class MenuItem extends SimpleYMLSection {

    /**
     * The package this item is inside
     */
    private final ConfigPackage pack;

    /**
     * The betonquest quest item this item is based on
     */
    private final Item item;

    /**
     * HashMap with a language as key and the corresponding description as value
     */
    private final HashMap<String, ItemDescription> descriptions;

    /**
     * Ids of all events that should be run on left click
     */
    private final List<EventID> left_click;

    /**
     * Ids of all events that should be run on right click
     */
    private final List<EventID> right_click;

    /**
     * Conditions that have to be matched to view the item
     */
    private final List<ConditionID> conditions;

    /**
     * If the menu should be closed when the item is clicked
     */
    private final boolean close;

    /**
     * Id of the item
     */
    private final String id;

    public MenuItem(ConfigPackage pack, String id, ConfigurationSection section) throws InvalidConfigurationException {
        super(id, section);
        try {
            this.id = id;
            this.pack = pack;
            //load item
            ItemID itemID = new ItemID(pack, getString("item").trim());
            VariableNumber amount;
            amount = new VariableNumber(pack.getName(), new DefaultSetting<String>("1") {
                @Override
                protected String of() throws Missing {
                    return getString("amount");
                }
            }.get());
            this.item = new Item(itemID, amount);
            // load description
            this.descriptions = new HashMap<>();
            try {
                if (section.isConfigurationSection("text")) {
                    for (String lang : section.getConfigurationSection("text").getKeys(false)) {
                        this.descriptions.put(lang, new ItemDescription(this.pack, getStringList("text." + lang)));
                    }
                    if (!this.descriptions.containsKey(Config.getLanguage()))
                        throw new Missing("text." + Config.getLanguage());
                } else {
                    this.descriptions.put(Config.getLanguage(),
                            new ItemDescription(this.pack, getStringList("text")));
                }
            } catch (Missing missing) {
            }
            //load events
            this.left_click = new ArrayList<>();
            this.right_click = new ArrayList<>();
            if (config.isConfigurationSection("click")) {
                try {
                    this.left_click.addAll(getEvents("click.left", pack));
                } catch (Missing e) {
                }
                try {
                    this.right_click.addAll(getEvents("click.right", pack));
                } catch (Missing e) {
                }
            } else {
                try {
                    List<EventID> l = getEvents("click", pack);
                    this.left_click.addAll(l);
                    this.right_click.addAll(l);
                } catch (Missing e) {
                }
            }
            //load display conditions
            this.conditions = new ArrayList<>();
            try {
                this.conditions.addAll(getConditions("conditions", pack));
            } catch (Missing e) {
            }
            try {
                this.conditions.addAll(getConditions("condition", pack));
            } catch (Missing e) {
            }
            //load if menu should close when item is clicked
            this.close = new DefaultSetting<Boolean>(RPGMenu.getConfiguration().defaultCloseOnClick) {
                @Override
                protected Boolean of() throws Missing, Invalid {
                    return getBoolean("close");
                }
            }.get();
        } catch (ObjectNotFoundException | InstructionParseException e) {
            throw new InvalidConfigurationException(e.getMessage());
        }
    }

    /**
     * Action that happens on click
     *
     * @param player that has clicked the item
     * @param type   type of the click action
     * @return if the menu should be closed after this operation
     */
    public boolean onClick(Player player, ClickType type) {
        switch (type) {
            case RIGHT:
            case SHIFT_RIGHT:
                for (EventID eventID : this.right_click) {
                    Log.debug("Item " + name + ": Run event " + eventID);
                    BetonQuest.event(PlayerConverter.getID(player), eventID);
                }
                return this.close;
            case LEFT:
            case SHIFT_LEFT:
                for (EventID eventID : this.left_click) {
                    Log.debug("Item " + name + ": Run event " + eventID);
                    BetonQuest.event(PlayerConverter.getID(player), eventID);
                }
                return this.close;
            default:
                return false;
        }
    }

    /**
     * Checks if this item should be displayed to the player
     *
     * @param player that should get the item displayed
     * @return true if all display conditions are met, false otherwise
     */
    public boolean display(Player player) {
        for (ConditionID condition : this.conditions) {
            if (!BetonQuest.condition(PlayerConverter.getID(player), condition)) {
                Log.debug("Item " + name + " wont be displayed: condition" + condition + " returned false.");
                return false;
            } else {
                Log.debug("Item " + name + ": condition " + condition + " returned true");
            }
        }
        return true;
    }

    /**
     * Generates the menu item for a specific player
     *
     * @param player the player this item will be displayed to
     * @return the item as a bukkit item stack
     */
    public ItemStack generateItem(Player player) {
        try {
            String playerId = PlayerConverter.getID(player);
            String lang = BetonQuest.getInstance().getPlayerData(playerId).getLanguage();
            ItemStack item = this.item.generate(playerId);
            ItemMeta meta = item.getItemMeta();
            if (!descriptions.isEmpty()) {
                ItemDescription description = this.descriptions.get(lang);
                if (description == null) description = this.descriptions.get(Config.getLanguage());
                try {
                    meta.setDisplayName(description.getDisplayName(playerId));
                    meta.setLore(description.getLore(playerId));
                    item.setItemMeta(meta);
                } catch (NullPointerException npe) {
                    Log.error("Couldn't add custom text to §7" + id + "§4: No text for language §7" + Config.getLanguage() + "§4 " +
                            "specified");
                }
            }
            return item;
        } catch (QuestRuntimeException qre) {
            Log.error("QuestRuntimeException while creating §7" + id + "§4: " + qre.getMessage());
            return new ItemStack(Material.AIR);
        }
    }

    /**
     * @return the items internal id
     */
    public String getId() {
        return id;
    }

    /**
     * Extended, static copy of pl.betoncraft.betonquest.Instruction.Item for easier quest item handling
     */
    public static class Item {

        private ItemID itemID;
        private QuestItem questItem;
        private VariableNumber amount;

        public Item(ItemID itemID, VariableNumber amount) throws InstructionParseException {
            this.itemID = itemID;
            this.questItem = new QuestItem(itemID);
            this.amount = amount;
        }

        public ItemID getID() {
            return itemID;
        }

        public QuestItem getItem() {
            return questItem;
        }

        public boolean isItemEqual(ItemStack item) {
            return questItem.compare(item);
        }

        public VariableNumber getAmount() {
            return amount;
        }

        public ItemStack generate(String playerID) throws QuestRuntimeException {
            return questItem.generate(amount.getInt(playerID));
        }
    }

}
