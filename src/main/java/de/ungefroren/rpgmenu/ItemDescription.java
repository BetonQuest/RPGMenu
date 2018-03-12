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

import de.ungefroren.rpgmenu.utils.Utils;
import pl.betoncraft.betonquest.BetonQuest;
import pl.betoncraft.betonquest.InstructionParseException;
import pl.betoncraft.betonquest.config.ConfigPackage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Contains the description of a item in a menu. Variables are parsed and color codes are replaced.
 * <p>
 * Created on 27.01.2018
 *
 * @author Jonas Blocher
 */
public class ItemDescription {

    private final ConfigPackage pack;
    private final List<Line> lines;

    public ItemDescription(ConfigPackage pack, Collection<String> content) throws InstructionParseException {
        this.pack = pack;
        this.lines = new ArrayList<>();
        for (String line : content) {
            new Line(line);
        }
    }

    /**
     * Receive display name of item for specific player
     *
     * @param playerID id of the player
     * @return
     */
    public String getDisplayName(String playerID) {
        Line displayName = this.lines.get(0);
        if (displayName == null) return null;
        return displayName.resolve(playerID);
    }

    /**
     * Receive lore of the item for specific player
     *
     * @param playerID id of the player
     * @return
     */
    public List<String> getLore(String playerID) {
        List<Line> lines = this.lines.subList(1, this.lines.size());
        if (lines.isEmpty()) return new ArrayList<>();
        List<String> lore = new ArrayList<>(lines.size());
        for (Line line : lines) {
            lore.add(line.resolve(playerID));
        }
        return lore;
    }

    /**
     * Helper class that simplifies parsing variables for a line
     */
    private class Line {

        private final String line;
        private final List<String> variables;

        public Line(String line) throws InstructionParseException {
            //set line
            this.line = Utils.translateAlternateColorcodes('&', line);
            //find variables
            this.variables = new ArrayList<>();
            for (String variable : BetonQuest.resolveVariables(line)) {
                try {
                    BetonQuest.createVariable(pack, variable);
                } catch (InstructionParseException e) {
                    throw new InstructionParseException("Could not create '" + variable + "' variable: " + e.getMessage());
                }
                if (!variables.contains(variable))
                    variables.add(variable);
            }
            lines.add(this);
        }

        /**
         * Resolves all variables in this line for specified player
         *
         * @param playerID id of a player
         * @return
         */
        public String resolve(String playerID) {
            String line = this.line;
            for (String variable : variables) {
                line = line.replace(variable, BetonQuest.getInstance().getVariableValue(pack.getName(), variable, playerID));
            }
            return line;
        }
    }

}
