package com.example.sabrina.hsvcolorpicker.db;

import java.util.Arrays;
import java.util.HashSet;

public class ColorTable {
    // Column names
    public static final String TABLE_COLOR = "Color";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_NAME = "Name";
    public static final String COLUMN_HEX = "Hex";
    public static final String COLUMN_HUE = "Hue";
    public static final String COLUMN_SATURATION = "Saturation";
    public static final String COLUMN_VALUE = "Value";

    private static HashSet<String> VALID_COLUMN_NAMES;

    static {
        String[] validNames = {
                TABLE_COLOR,
                COLUMN_ID,
                COLUMN_NAME,
                COLUMN_HEX,
                COLUMN_HUE,
                COLUMN_SATURATION,
                COLUMN_VALUE
        };

        VALID_COLUMN_NAMES = new HashSet<String>(Arrays.asList( validNames ));
    }

    public static void validateProjection(String[] projection) {

        if (projection != null) {
            HashSet<String> requestedColumns = new HashSet<String>(Arrays.asList(projection));

            // check if all columns which are requested are available
            if ( !VALID_COLUMN_NAMES.containsAll( requestedColumns ) ) {
                throw new IllegalArgumentException("Unknown columns in projection");
            }
        }
    }
}

