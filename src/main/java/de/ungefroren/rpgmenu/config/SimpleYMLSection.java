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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;

import pl.betoncraft.betonquest.ConditionID;
import pl.betoncraft.betonquest.EventID;
import pl.betoncraft.betonquest.ObjectNotFoundException;
import pl.betoncraft.betonquest.config.ConfigPackage;

/**
 * Abstract class to help parsing of yml config files
 * <p>
 * Created on 06.09.2017
 *
 * @author Jonas Blocher
 */
public abstract class SimpleYMLSection {

    protected final ConfigurationSection config;
    protected final String name;

    public SimpleYMLSection(String name, ConfigurationSection config) throws InvalidConfigurationException {
        this.config = config;
        this.name = name;
        if (config == null || config.getKeys(false) == null || config.getKeys(false).size() == 0)
            throw new InvalidSimpleConfigException("RPGMenuConfig is invalid or empty!");
    }

    /**
     * Parse string from config file
     *
     * @param key where to search
     * @throws Missing if string is not given
     */
    protected String getString(String key) throws Missing {
        String s = config.getString(key);
        if (s != null) return s;
        else throw new Missing(key);
    }

    /********************************************************
     *                                                      *
     *                  STRING LISTS                        *
     *                                                      *
     ********************************************************/

    /**
     * Parse a list of strings from config file
     *
     * @param key where to search
     * @throws Missing if no list is not given
     */
    protected List<String> getStringList(String key) throws Missing {
        List<String> list = config.getStringList(key);
        if (list != null && list.size() != 0) return list;
        else throw new Missing(key);
    }

    /**
     * Parse a list of multiple strings, separated by ',' from config file
     *
     * @param key where to search
     * @throws Missing if no strings are given
     */
    protected List<String> getStrings(String key) throws Missing {
        List<String> list = new ArrayList<>();
        String[] args = getString(key).split(",");
        for (String arg : args) {
            arg = arg.trim();
            if (arg.length() != 0) {
                list.add(arg);
            }
        }
        return list;
    }

    /********************************************************
     *                                                      *
     *                       NUMBERS                        *
     *                                                      *
     ********************************************************/

    /**
     * Parse an integer from config file
     *
     * @param key where to search
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not an integer
     */
    protected int getInt(String key) throws Missing, Invalid {
        String s = this.getString(key);
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            throw new Invalid(key, "Invalid number format for '" + s + "'");
        }
    }

    /**
     * Parse a double from config file
     *
     * @param key where to search
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not a double
     */
    protected double getDouble(String key) throws Missing, Invalid {
        String s = this.getString(key);
        try {
            return Double.parseDouble(s);
        } catch (NumberFormatException e) {
            throw new Invalid(key, "Invalid number format for '" + s + "'");
        }
    }

    /**
     * Parse a long from config file
     *
     * @param key where to search
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not a long
     */
    protected long getLong(String key) throws Missing, Invalid {
        String s = this.getString(key);
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            throw new Invalid(key, "Invalid number format for '" + s + "'");
        }
    }

    /********************************************************
     *                                                      *
     *                      OTHER                           *
     *                                                      *
     ********************************************************/

    /**
     * Parse a boolean from config file
     *
     * @param key where to search
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not a boolean
     */
    protected boolean getBoolean(String key) throws Missing, Invalid {
        String s = this.getString(key);
        if (s.trim().equalsIgnoreCase("true")) return true;
        else if (s.trim().equalsIgnoreCase("false")) return false;
        else throw new Invalid(key);
    }

    /**
     * Parse an enum value from config file
     *
     * @param key      where to search
     * @param enumType type of the enum
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not of given type
     */
    protected <T extends Enum<T>> T getEnum(String key, Class<T> enumType) throws Missing, Invalid {
        String s = this.getString(key).toUpperCase().replace(" ", "_");
        try {
            return Enum.valueOf(enumType, s);
        } catch (IllegalArgumentException e) {
            throw new Invalid(key, "'" + s + "' isn't a " + enumType.getName());
        }
    }


    /**
     * Parse a material from config file
     *
     * @param key where to search
     * @throws Missing if nothing is given
     * @throws Invalid if given string is not a material
     */
    protected Material getMaterial(String key) throws Missing, Invalid {
        String s = this.getString(key);
        if (key.trim().matches("\\d+")) {
            throw new Invalid(key, "Material numbers can no longer be supported! Please use the names instead.");
        }
        Material m;
        try {
            m  = Material.matchMaterial(s.replace(" ", "_"));
            if (m == null) {
                m = Material.matchMaterial(s.replace(" ", "_"), true);
            }
        } catch (LinkageError error) {
            //pre 1.13
            m = Material.getMaterial(s.toUpperCase().replace(" ", "_"));
        }
        if (m != null) return m;
        else throw new Invalid(key, "'" + s + "' isn't a material");
    }

    /**
     * Parse a list of events from config file
     *
     * @param key  where to search
     * @param pack configuration package of this file
     * @throws Missing if nothing is given
     * @throws Invalid if one of the events can't be found
     */
    protected List<EventID> getEvents(String key, ConfigPackage pack) throws Missing, Invalid {
        List<String> strings = getStrings(key);
        List<EventID> events = new ArrayList<>(strings.size());
        for (String string : strings) {
            try {
                events.add(new EventID(pack, string));
            } catch (ObjectNotFoundException e) {
                throw new Invalid(key, e);
            }
        }
        return events;
    }

    /**
     * Parse a list of conditions from config file
     *
     * @param key  where to search
     * @param pack configuration package of this file
     * @throws Missing if nothing is given
     * @throws Invalid if one of the conditions can't be found
     */
    protected List<ConditionID> getConditions(String key, ConfigPackage pack) throws Missing, Invalid {
        List<String> strings = getStrings(key);
        List<ConditionID> conditions = new ArrayList<>(strings.size());
        for (String string : strings) {
            try {
                conditions.add(new ConditionID(pack, string));
            } catch (ObjectNotFoundException e) {
                throw new Invalid(key, e);
            }
        }
        return conditions;
    }

    /**
     * A config setting which uses a given default value if not set
     *
     * @param <T> the type of the setting
     */
    protected abstract class DefaultSetting<T> {

        private T value;

        public DefaultSetting(T defaultValue) throws Invalid {
            try {
                value = of();
            } catch (Missing missing) {
                value = defaultValue;
            }
        }

        protected abstract T of() throws Missing, Invalid;

        public final T get() {
            return value;
        }
    }

    /**
     * A config setting which doesn't throw a Missing exception if not specified
     *
     * @param <T> the type of the setting
     */
    protected abstract class OptionalSetting<T> {

        private Optional<T> optional;

        public OptionalSetting() throws Invalid {
            try {
                optional = Optional.of(of());
            } catch (Missing missing) {
                optional = Optional.empty();
            }
        }

        protected abstract T of() throws Missing, Invalid;

        public final Optional<T> get() {
            return optional;
        }
    }

    /**
     * Thrown when the config could not be loaded due to an error
     */
    public class InvalidSimpleConfigException extends InvalidConfigurationException {

        private final String message;
        private final String cause;

        public InvalidSimpleConfigException(String cause) {
            this.cause = "  §c" + cause;
            this.message = "§4Could not load §7" + getName() + "§4:\n" + this.cause;
        }

        public InvalidSimpleConfigException(InvalidSimpleConfigException e) {
            this.cause = "  §4Error in §7" + e.getName() + "§4:\n" + e.cause;
            this.message = "Could not load §7" + getName() + "§4\n" + this.cause;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        public String getName() {
            return name;
        }
    }

    /**
     * Thrown when a setting is missing
     */
    public class Missing extends InvalidSimpleConfigException {

        public Missing(String missingSetting) {
            super("RPGMenuConfig setting §7" + missingSetting + "§c is missing!");
        }
    }

    /**
     * Thrown when a setting is invalid
     */
    public class Invalid extends InvalidSimpleConfigException {

        public Invalid(String invalidSetting) {
            super("RPGMenuConfig setting §7" + invalidSetting + "§c is invalid!");
        }

        public Invalid(String invalidSetting, String cause) {
            super("RPGMenuConfig setting §7" + invalidSetting + "§c is invalid: §7" + cause);
        }

        public Invalid(String invalidSetting, Throwable cause) {
            super("RPGMenuConfig setting §7" + invalidSetting + "§c is invalid: §7" + cause.getMessage());
        }
    }
}
