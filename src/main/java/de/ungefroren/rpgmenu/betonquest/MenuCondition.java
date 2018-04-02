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

import org.bukkit.entity.Player;

import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.ObjectNotFoundException;
import pl.betoncraft.betonquest.QuestRuntimeException;
import pl.betoncraft.betonquest.api.Condition;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import de.ungefroren.rpgmenu.MenuID;
import de.ungefroren.rpgmenu.RPGMenu;

/**
 * Checks if a player has opened a menu
 * <p>
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuCondition extends Condition {

    private MenuID menuID;

    public MenuCondition(Instruction instruction) throws InstructionParseException {
        super(instruction);
        String id = instruction.getOptional("id");
        try {
            this.menuID = (id == null) ? null : new MenuID(instruction.getPackage(), id);
        } catch (ObjectNotFoundException e) {
            throw new InstructionParseException("Error while parsing id optional: Error while loading menu: " + e.getMessage());
        }
    }

    @Override
    public boolean check(String playerId) throws QuestRuntimeException {
        Player player = PlayerConverter.getPlayer(playerId);
        return RPGMenu.hasOpenedMenu(player, menuID);
    }
}
