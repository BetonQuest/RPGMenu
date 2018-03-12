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

package de.ungefroren.rpgmenu.utils;

import de.ungefroren.rpgmenu.RPGMenu;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Various utilities.
 * <p>
 * Created on 13.01.2018
 *
 * @author Jonas Blocher
 */
public class Utils {

    /**
     * Translate alternate color codes of a string list
     *
     * @param colorCodeChar the char which should be replaced with <b>§</b>
     * @param lines         the string list to translate
     * @return <b>lines</b> with <b>colorCodeChar</b> replaced by <b>§</b>
     */
    public static List<String> translateAlternateColorcodes(char colorCodeChar, List<String> lines) {
        List<String> translated = new ArrayList<>(lines.size());
        for (String line : lines) {
            translated.add(Utils.translateAlternateColorcodes(colorCodeChar, line));
        }
        return translated;
    }

    /**
     * Translate alternate color codes of a string
     *
     * @param colorCodeChar the char which should be replaced with <b>§</b>
     * @param line          the string list to translate
     * @return <b>line</b> with <b>colorCodeChar</b> replaced by <b>§</b>
     */
    public static String translateAlternateColorcodes(char colorCodeChar, String line) {
        Matcher matcher = Pattern.compile("&[0-9a-zA-Z]").matcher(line);
        while (matcher.find()) {
            line = line.substring(0, matcher.start()) + "§" + line.substring(matcher.start() + 1);
        }
        return line;
    }

    /**
     * Allows you accessing and modifying private fields
     *
     * @param clazz the class which has the field
     * @param name  the field you want to access
     * @return the field for the given class with the given name
     * @throws NoSuchFieldException if the field with the specified name cant be found
     */
    public static Field getField(Class<?> clazz, String name) throws NoSuchFieldException {
        do {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.getName().equals(name)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        throw new NoSuchFieldException("Can't find field " + name);
    }

    /**
     * Allows accessing and calling private methods
     *
     * @param clazz       the class which has the method
     * @param name        the name of the method
     * @param paramlength the amount of parameters the method has
     * @return the method for the given class with given name and parameters
     * @throws NoSuchMethodException if the method with the specified name and parameters cant be found
     */
    public static Method getMethod(Class<?> clazz, String name, int paramlength) throws NoSuchMethodException {
        do {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.getName().equals(name) && (method.getParameterTypes().length == paramlength)) {
                    method.setAccessible(true);
                    return method;
                }
            }
        } while ((clazz = clazz.getSuperclass()) != null);
        throw new NoSuchMethodException("Can't find method " + name + " with " + paramlength + " parameters");
    }

    /**
     * Save a resource at a given path
     *
     * @param directory    the file where to save the resource
     * @param resourceName name of the resource
     * @throws IOException If an I/O error occurs
     */
    public static void saveResource(File directory, String resourceName) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(RPGMenu.getInstance().getResource(resourceName)));
        BufferedWriter bw = new BufferedWriter(new FileWriter(directory));
        int i;
        while ((i = br.read()) != -1) bw.write(i);
        bw.close();
        br.close();
    }

    /**
     * @param string name of a class which should be loaded
     * @return if the class exists
     */
    public static boolean doesClassExist(String string) {
        try {
            Class.forName(string);
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
}
