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


import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import de.ungefroren.rpgmenu.events.MenuClickEvent;
import de.ungefroren.rpgmenu.events.MenuCloseEvent;
import de.ungefroren.rpgmenu.utils.Log;

/**
 * Class representing a menu which is currently displayed to a player
 * <p>
 * Created on 11.03.2018
 *
 * @author Jonas Blocher
 */
public class OpenedMenu implements Listener {

    /**
     * Hashmap containing all currently opened menus
     */
    private static HashMap<UUID, OpenedMenu> openedMenus = new HashMap<>();
    private final UUID playerId;
    private final Menu data;
    private MenuItem[] items;
    private boolean closed = false;

    public OpenedMenu(Player player, Menu menu) {
        // If player already has an open menu we close it first
        OpenedMenu current = getMenu(player);
        if (current != null) {
            current.close();
        }

        this.data = menu;
        this.playerId = player.getUniqueId();
        Inventory inventory = Bukkit.createInventory(null, data.getSize(), data.getTitle());
        this.update(player, inventory);
        player.openInventory(inventory);
        Bukkit.getPluginManager().registerEvents(this, RPGMenu.getInstance());
        openedMenus.put(playerId, this);
        this.data.runOpenEvents(player);
    }

    /**
     * Returns the menu a player has opened
     *
     * @param player the player to check for
     * @return the menu the player has opened or null if he has no open menus
     */
    public static OpenedMenu getMenu(Player player) {
        return openedMenus.get(player.getUniqueId());
    }

    /**
     * Closes the players menu if he has one open
     */
    protected static void closeMenu(Player player) {
        OpenedMenu menu = openedMenus.get(player.getUniqueId());
        if (menu == null) return;
        menu.close();
    }

    /**
     * Closes all currently opened menus
     * <p>
     * Called when the plugin unloads to prevent glitching menus
     */
    public static void closeAll() {
        for (OpenedMenu openedMenu : openedMenus.values()) {
            openedMenu.close();
        }
    }

    /**
     * @return true if menu was closed
     */
    public boolean isClosed() {
        return closed;
    }

    /**
     * @return the id of this menu
     */
    public MenuID getId() {
        return this.data.getID();
    }

    /**
     * @return the menu object containing all data
     */
    public Menu getData() {
        return this.data;
    }

    /**
     * @return the player the menu is displayed to
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(playerId);
    }

    /**
     * @return the inventory which shows the menu
     */
    public Inventory getInventory() {
        return this.getPlayer().getOpenInventory().getTopInventory();
    }

    /**
     * Closes the menu
     */
    public void close() {
        getPlayer().closeInventory();
        closed = true;
    }

    /**
     * (Re-)adds all items to the inventory
     *
     * @param player    the player the menu is displayed to
     * @param inventory the inventory showing the menu
     */
    public void update(Player player, Inventory inventory) {
        this.items = data.getItems(player);
        ItemStack[] content = new ItemStack[items.length];
        //add the items if display conditions are matched
        for (int i = 0; i < items.length; i++) {
            content[i] = (items[i] != null) ? items[i].generateItem(player) : new ItemStack(Material.AIR);
        }
        Log.debug("updated contents of menu " + getId() + " for " + player.getName());
        inventory.setContents(content);
    }

    /**
     * Readds all items to the inventory
     */
    public void update() {
        this.update(getPlayer(), getInventory());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player) event.getWhoClicked();
        if (!player.getUniqueId().equals(playerId)) return;
        event.setCancelled(true);
        Inventory inventory = event.getClickedInventory();
        //only continue if menu is clicked
        if (inventory == null || inventory instanceof PlayerInventory) return;
        MenuItem item = this.items[event.getSlot()];
        //only continue if a displayed item was clicked
        if (item == null) return;
        //only continue if click type is valid
        switch (event.getClick()) {
            case SHIFT_RIGHT:
            case RIGHT:
            case SHIFT_LEFT:
            case LEFT:
                break;
            default:
                return;
        }
        //call event
        MenuClickEvent clickEvent = new MenuClickEvent(player, getId(), event.getSlot(), item.getId(), event.getClick());
        Bukkit.getPluginManager().callEvent(clickEvent);
        Log.debug(player.getName() + " clicked on slot " + event.getSlot() + " with item " + item.getId() + " in menu " + getId());
        if (clickEvent.isCancelled()) {
            Log.debug("click of " + player.getName() + " in menu " + getId() + " was cancelled by a bukkit event listener");
            return;
        }
        //handle click
        boolean close = item.onClick(player, event.getClick());
        // If we are already closed then we are done
        if (closed) {
            return;
        }

        //if close was set close the menu
        if (close) this.close();
            // otherwise update the contents
        else this.update();
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!(event.getPlayer() instanceof Player)) return;
        Player player = (Player) event.getPlayer();
        if (!player.getUniqueId().equals(playerId)) return;
        //call event
        MenuCloseEvent closeEvent = new MenuCloseEvent(player, getId());
        Bukkit.getPluginManager().callEvent(closeEvent);
        Log.debug(player.getName() + " closed menu " + getId());
        //clean up
        HandlerList.unregisterAll(this);
        openedMenus.remove(playerId);
        //run close events
        this.data.runCloseEvents(player);
    }
}
