package com.leclowndu93150.hlsubstance.curios;

import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

public enum InventoryCurio {
    TOP("hlsubstance_top", "necklace", 77, 8, 135, 20),
    MIDDLE("hlsubstance_middle", "belt", 77, 26, 153, 20),
    BOTTOM("hlsubstance_bottom", "ring", 77, 44, 171, 20);

    private final String identifier;
    private final String defaultType;
    private final int x;
    private final int y;
    private final int creativeX;
    private final int creativeY;

    InventoryCurio(String identifier, String defaultType, int x, int y, int creativeX, int creativeY) {
        this.identifier = identifier;
        this.defaultType = defaultType;
        this.x = x;
        this.y = y;
        this.creativeX = creativeX;
        this.creativeY = creativeY;
    }

    public static Optional<InventoryCurio> byIdentifier(String identifier) {
        return Arrays.stream(values()).filter(curio -> curio.identifier.equals(identifier)).findFirst();
    }

    public String identifier() {
        return identifier;
    }

    public String defaultType() {
        return defaultType;
    }

    public String configKey() {
        return name().toLowerCase(Locale.ROOT);
    }

    public int x() {
        return x;
    }

    public int y() {
        return y;
    }

    public int creativeX() {
        return creativeX;
    }

    public int creativeY() {
        return creativeY;
    }
}
