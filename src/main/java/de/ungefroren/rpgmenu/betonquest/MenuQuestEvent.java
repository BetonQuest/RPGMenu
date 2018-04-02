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
import pl.betoncraft.betonquest.api.QuestEvent;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import de.ungefroren.rpgmenu.MenuID;
import de.ungefroren.rpgmenu.RPGMenu;

/**
 * Event to open or close menus
 * <p>
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuQuestEvent extends QuestEvent {

    private final Operation operation;
    private MenuID menu;
    public MenuQuestEvent(Instruction instruction) throws InstructionParseException {
        super(instruction);
        this.operation = instruction.getEnum(Operation.class);
        if (this.operation == Operation.OPEN) {
            try {
                this.menu = new MenuID(instruction.getPackage(), instruction.next());
            } catch (ObjectNotFoundException e) {
                throw new InstructionParseException("Error while parsing 2 argument: Error while loading menu: " + e.getMessage());
            }
        }
    }

    @Override
    public void run(String playerID) throws QuestRuntimeException {
        Player player = PlayerConverter.getPlayer(playerID);
        if (operation == Operation.OPEN) {
            RPGMenu.openMenu(player, menu);
        } else {
            RPGMenu.closeMenu(player);
        }
    }

    public enum Operation {
        OPEN,
        CLOSE
    }
}
