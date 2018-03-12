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

import pl.betoncraft.betonquest.ID;
import pl.betoncraft.betonquest.ObjectNotFoundException;
import pl.betoncraft.betonquest.config.ConfigPackage;

import java.io.File;
import java.util.Objects;

/**
 * Id of a menu
 *
 * Created on 10.02.2018
 *
 * @author Jonas Blocher
 */
public class MenuID extends ID {

    private final File file;

    public MenuID(ConfigPackage pack, String id) throws ObjectNotFoundException {
        super(pack, id);
        super.rawInstruction = null;
        //find file
        file = new File(super.pack.getFolder(), "menus" + File.separator + super.getBaseID() + ".yml");
        if (!file.exists()) throw new ObjectNotFoundException("Menu '" + getFullID() + "' is not defined");
    }

    /**
     * File where the menus config is located on disk
     */
    public File getFile() {
        return file;
    }


    @Override
    public int hashCode() {
        return Objects.hash(super.getBaseID(), super.pack.getName());
    }
}
