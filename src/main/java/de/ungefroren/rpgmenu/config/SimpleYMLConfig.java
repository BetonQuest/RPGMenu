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

package de.ungefroren.rpgmenu.config;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import pl.betoncraft.betonquest.utils.Debug;

import java.io.File;
import java.io.IOException;

/**
 * Created on 24.01.2018
 *
 * @author Jonas Blocher
 */
public abstract class SimpleYMLConfig extends SimpleYMLSection {

    protected final File file;

    public SimpleYMLConfig(File file) throws InvalidConfigurationException {
        this(file.getName(), file);
    }

    public SimpleYMLConfig(String name, File file) throws InvalidConfigurationException {
        this(name, file, YamlConfiguration.loadConfiguration(file));
    }

    public SimpleYMLConfig(File file, FileConfiguration config) throws InvalidConfigurationException {
        this(file.getName(), file, config);
    }

    public SimpleYMLConfig(String name, File file, FileConfiguration config) throws InvalidConfigurationException {
        super(name, config);
        this.file = file;
    }

    public boolean save() {
        try {
            ((FileConfiguration) super.config).save(this.file);
            return true;
        } catch (IOException e) {
            Debug.error(e.getMessage());
            return false;
        }
    }
}
