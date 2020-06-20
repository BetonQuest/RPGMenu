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

import de.ungefroren.rpgmenu.utils.Log;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.Instruction;
import pl.betoncraft.betonquest.api.Objective;
import pl.betoncraft.betonquest.exceptions.InstructionParseException;
import pl.betoncraft.betonquest.exceptions.ObjectNotFoundException;
import pl.betoncraft.betonquest.utils.PlayerConverter;

import de.ungefroren.rpgmenu.Menu;
import de.ungefroren.rpgmenu.MenuID;
import de.ungefroren.rpgmenu.RPGMenu;
import de.ungefroren.rpgmenu.events.MenuOpenEvent;

/**
 * Completed if menu with given id is opened
 * <p>
 * Created on 16.03.2018.
 *
 * @author Jonas Blocher
 */
public class MenuObjective extends Objective implements Listener {

    private final MenuID menuID;

    public MenuObjective(Instruction instruction) throws InstructionParseException {
        super(instruction);
        template = ObjectiveData.class;
        try {
            this.menuID = new MenuID(instruction.getPackage(), instruction.next());
        } catch (ObjectNotFoundException e) {
            throw new InstructionParseException("Error while parsing 1 argument: Error while loading menu: " + e.getMessage());
        }
    }

    @EventHandler
    public void onMenuOpen(MenuOpenEvent event) {
        final String playerID = PlayerConverter.getID(event.getPlayer());
        if (!containsPlayer(playerID)) return;
        if (!event.getMenu().equals(menuID)) return;
        if (!checkConditions(playerID)) return;
        this.completeObjective(playerID);
    }

    @Override
    public void start() {
        Bukkit.getPluginManager().registerEvents(this, BetonQuest.getInstance());
    }

    @Override
    public void stop() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public String getDefaultDataInstruction() {
        return "";
    }

    @Override
    public String getProperty(String name, String playerID) {
        if (name.equalsIgnoreCase("menu")) {
            Menu menuData = RPGMenu.getInstance().getMenu(menuID);
            if (menuData == null) {
                Log.debug("Error while getting menu property in '" + instruction.getID() + "' objective: "
                        + "menu with id " + menuID + " isn't loaded");
                return "";
            }
            return menuData.getTitle();
        }
        return "";
    }
}
