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
package de.ungefroren.rpgmenu.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.ClickType;

import de.ungefroren.rpgmenu.MenuID;

/**
 * Called whenever a item in a menu is clicked by a player
 * <p>
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuClickEvent extends MenuEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private final int slot;
    private final String itemId;
    private final ClickType clickType;
    private boolean cancelled = false;

    public MenuClickEvent(Player who, MenuID menu, int slot, String itemId, ClickType clickType) {
        super(who, menu);
        this.clickType = clickType;
        this.itemId = itemId;
        this.slot = slot;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    /**
     * @return the clicked slot
     */
    public int getSlot() {
        return slot;
    }

    /**
     * @return the internal id of the item in the clicked slot
     */
    public String getItemId() {
        return itemId;
    }

    /**
     * @return the type of the click preformed (Possible values are: RIGHT, SHIFT_RIGHT, LEFT, SHIFT_LEFT)
     */
    public ClickType getClickType() {
        return clickType;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        cancelled = b;
    }
}
