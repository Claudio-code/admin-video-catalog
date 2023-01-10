package com.admin.catalog.domain.util;

import lombok.experimental.UtilityClass;

import java.time.Instant;

import static java.time.temporal.ChronoUnit.MICROS;

@UtilityClass
public class InstantUtils {
    public static Instant now() {
        return Instant.now().truncatedTo(MICROS);
    }
}
