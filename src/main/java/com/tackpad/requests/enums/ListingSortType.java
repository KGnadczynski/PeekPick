package com.tackpad.requests.enums;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Listing sort type.
 * @author Przemek
 *
 */
public enum ListingSortType {

    /** Distance. */
    DISTANCE,

    /** Alphabetical. */
    CREATE_DATE;

    public static ListingSortType  convertFromString(String name) {

        if (name == null) {
            return null;
        }

        try {
            return valueOf(name);
        } catch (IllegalArgumentException e) {
            Logger.getGlobal().log(Level.WARNING, "Wrong value " + name);
            return null;
        }

    }

}
