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
import org.bukkit.event.HandlerList;

import de.ungefroren.rpgmenu.MenuID;

/**
 * Called whenever a menu is closed
 * <p>
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuCloseEvent extends MenuEvent {

    private static final HandlerList handlers = new HandlerList();

    public MenuCloseEvent(Player who, MenuID menu) {
        super(who, menu);
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
