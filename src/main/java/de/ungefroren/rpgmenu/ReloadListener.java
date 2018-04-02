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

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import pl.betoncraft.betonquest.api.LoadDataEvent;

/**
 * Created on 11.03.2018
 *
 * @author Jonas Blocher
 */
public class ReloadListener implements Listener {

    public ReloadListener() {
        Bukkit.getPluginManager().registerEvents(this, RPGMenu.getInstance());
    }

    @EventHandler
    public void onReload(LoadDataEvent event) {
        RPGMenu.getInstance().reloadData();
    }
}
