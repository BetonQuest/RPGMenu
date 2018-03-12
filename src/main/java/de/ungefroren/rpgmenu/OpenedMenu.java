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


import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

/**
 * Created on 11.03.2018
 *
 * @author Jonas Blocher
 */
public class OpenedMenu implements Listener {

    private final UUID playerId;
    private final Menu data;

    public OpenedMenu(Player player, Menu menu) {
        this.data = menu;
        this.playerId = player.getUniqueId();



    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getWhoClicked().getUniqueId().equals(playerId)) return;

    }
}
