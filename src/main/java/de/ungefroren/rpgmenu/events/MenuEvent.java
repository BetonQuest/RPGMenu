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
import org.bukkit.event.player.PlayerEvent;

/**
 * Every event where a menu was involved
 *
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public abstract class MenuEvent extends PlayerEvent {

    private final MenuID menu;

    protected MenuEvent(Player who, MenuID menu) {
        super(who);
        this.menu = menu;
    }

    /**
     * @return the id of the menu that was involved in this event
     */
    public MenuID getMenu() {
        return menu;
    }
}
