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

import de.ungefroren.rpgmenu.config.RPGMenuConfig;
import de.ungefroren.rpgmenu.utils.Log;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created on 12.01.2018
 *
 * @author Jonas Blocher
 */
public class RPGMenu extends JavaPlugin {

    private static RPGMenu instance;

    private RPGMenuConfig config;

    public static RPGMenuConfig getConfiguration() {
        return instance.config;
    }

    public static RPGMenu getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;
        super.onEnable();
        try {
            this.config = new RPGMenuConfig();
        } catch (InvalidConfigurationException e) {
            Log.error(e);

        }
    }

    public void reloadData() {

    }
}
