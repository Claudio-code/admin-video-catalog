package com.admin.catalog.domain;

import java.util.UUID;

public abstract class Identifier {

    public abstract String getValue();

    protected static String uuid() {
        return UUID.randomUUID().toString().toLowerCase().replace("-", "");
    }

}
