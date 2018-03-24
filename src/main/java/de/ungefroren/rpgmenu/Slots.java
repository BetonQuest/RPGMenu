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

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created on 21.03.2018.
 *
 * @author Jonas Blocher
 */
public class Slots {

    private final int start;
    private final int end;
    private final List<MenuItem> items;
    private final Type type;

    public Slots(String slots, List<MenuItem> items) throws IllegalArgumentException {
        if (slots.matches("\\d+")) {
            this.type = Type.SINGLE;
            this.start = Integer.parseInt(slots);
            this.end = start;
        } else if (slots.matches("\\d+-\\d+")) {
            this.type = Type.LINEAR_LIST;
            int index = slots.indexOf("-");
            this.start = Integer.parseInt(slots.substring(0, index));
            this.end = Integer.parseInt(slots.substring(index + 1));
        } else throw new IllegalArgumentException(slots + " is not a valid slot identifier");
        this.items = items;
    }

    public static void checkSlots(Iterable<Slots> slots) throws SlotDoubledException {
        Set<Integer> all = new HashSet<>();
        for (Slots s : slots) {
            for (int slot : s.getSlots()) {
                if (all.contains(slot)) throw new SlotDoubledException(slot, s.toString());
                else all.add(slot);
            }
        }
    }

    /**
     * @return a sorted list of all slots which are covered by this slots object
     */
    public List<Integer> getSlots() {
        List<Integer> slots = new ArrayList<>();
        for (int i = start; i < end; i++) {
            slots.add(i);
        }
        return slots;
    }

    /**
     * @param slot the slot to check for
     * @return if this slots object covers the given slot
     */
    public boolean containsSlot(int slot) {
        return (slot <= end && slot >= start);
    }

    /**
     * @return all items assigned to the slots covered by this object
     */
    public List<MenuItem> getItems() {
        return items;
    }

    /**
     * @param player the player for which these slots should get displayed for
     * @return all items which should be shown to the specified player of the slots covered by this object
     */
    public List<MenuItem> getItems(Player player) {
        List<MenuItem> items = new ArrayList<>();
        for (MenuItem item : this.items) {
            if (item.display(player)) items.add(item);
        }
        return items;
    }

    /**
     * @param slot the index of the slot in the menu
     * @return the index of the given slot within this collection of slots, -1 if slot is not within this collection
     */
    public int getIndex(int slot) {
        switch (type) {
            case SINGLE:
                if (slot != start) return -1;
                else return 0;
            case LINEAR_LIST:
                if (slot > end || slot < start) return -1;
                else return slot - start;
            default:
                return -1;
        }
    }

    /**
     * @param player the player for which these slots should get displayed for
     * @param slot   the slot which should contain this item
     * @return the menu item which should be displayed in the given slot to the player
     */
    public MenuItem getItem(Player player, int slot) {
        int index = this.getIndex(slot);
        if (index == -1) throw new RuntimeException("Invalid slot for Slots '" + toString() + "': " + slot);
        try {
            return items.get(index);
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * @return the type of this slots object
     */
    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        switch (type) {
            case SINGLE:
                return String.valueOf(start);
            case LINEAR_LIST:
                return start + "-" + end;
            default:
                return super.toString();
        }
    }

    public enum Type {
        /**
         * A single slot
         */
        SINGLE,

        /**
         * Multiple slots ordered in a row, one behind each other
         */
        LINEAR_LIST
    }

    public static class SlotDoubledException extends Exception {

        private final int slot;
        private final String slots;

        public SlotDoubledException(int slot, String slots) {
            super("slot " + slot + " was already specified");
            this.slots = slots;
            this.slot = slot;
        }

        public int getSlot() {
            return slot;
        }

        public String getSlots() {
            return slots;
        }
    }
}
