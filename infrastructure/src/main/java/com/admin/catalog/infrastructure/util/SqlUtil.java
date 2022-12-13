package com.admin.catalog.infrastructure.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SqlUtil {

    public static String upper(final String term) {
        return term == null ? null : term.toUpperCase();
    }

    public static String like(final String term) {
        return term == null ? null : "%" + term + "%";
    }

}
