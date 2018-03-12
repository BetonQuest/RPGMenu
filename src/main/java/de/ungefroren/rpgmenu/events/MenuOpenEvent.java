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

import de.ungefroren.rpgmenu.MenuID;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;

/**
 * Called whenever a menu is opened
 *
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuOpenEvent extends MenuEvent implements Cancellable {

    private static final HandlerList handlers = new HandlerList();

    private boolean cancelled = false;

    public MenuOpenEvent(Player who, MenuID menu) {
        super(who, menu);
    }

    public static HandlerList getHandlerList() {
        return handlers;
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
