package com.admin.catalog.domain.util;

import java.util.UUID;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IdUtils {

    public static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }

}
