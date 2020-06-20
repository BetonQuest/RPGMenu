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
package de.ungefroren.rpgmenu.betonquest;

import de.ungefroren.rpgmenu.OpenedMenu;
import org.bukkit.entity.Player;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Variable;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.utils.PlayerConverter;

/**
 * Returns the title of the players currently opened menu
 * <p>
 * Created on 17.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuVariable extends Variable {

    public MenuVariable(Instruction instruction) throws InstructionParseException {
        super(instruction);
    }

    @Override
    public String getValue(String playerID) {
        Player player = PlayerConverter.getPlayer(playerID);
        OpenedMenu menu = OpenedMenu.getMenu(player);
        if (menu == null) return "";
        return menu.getData().getTitle();
    }
}
