package com.sdk4.boot;

import com.sdk4.boot.db.IdKeyUuidGenerator;

/**
 * @author sh
 */
public class Sdk4BootConstants {
    private Sdk4BootConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String DB_IDKEY_UUID_GENERATOR = IdKeyUuidGenerator.class.getName();

}
