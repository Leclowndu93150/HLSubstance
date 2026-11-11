package com.leclowndu93150.hlsubstance.curios;

import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.EnumMap;
import java.util.Map;

public final class InventoryCuriosConfig {

    public static final ModConfigSpec SPEC;

    private static final Map<InventoryCurio, ModConfigSpec.ConfigValue<String>> ACCEPTED_TYPES = new EnumMap<>(InventoryCurio.class);

    private static Map<InventoryCurio, String> acceptedTypes = defaults();

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.comment("The three Curios slots stacked above the offhand slot of the player inventory.",
                        "Each value is the identifier of a Curios slot type (ring, necklace, belt, charm, back, ...).",
                        "The slot accepts the items that slot type accepts and shows its icon as background.")
                .push("inventoryCurios");
        for (InventoryCurio curio : InventoryCurio.values()) {
            ACCEPTED_TYPES.put(curio, builder.define(curio.configKey(), curio.defaultType(), InventoryCuriosConfig::isValidType));
        }
        builder.pop();

        SPEC = builder.build();
    }

    public static void onLoad(ModConfigEvent.Loading event) {
        if (event.getConfig().getSpec() == SPEC) refresh();
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        if (event.getConfig().getSpec() == SPEC) refresh();
    }

    public static String acceptedType(InventoryCurio curio) {
        return acceptedTypes.get(curio);
    }

    private static void refresh() {
        Map<InventoryCurio, String> types = new EnumMap<>(InventoryCurio.class);
        ACCEPTED_TYPES.forEach((curio, value) -> types.put(curio, value.get()));
        acceptedTypes = types;
    }

    private static Map<InventoryCurio, String> defaults() {
        Map<InventoryCurio, String> types = new EnumMap<>(InventoryCurio.class);
        for (InventoryCurio curio : InventoryCurio.values()) {
            types.put(curio, curio.defaultType());
        }
        return types;
    }

    private static boolean isValidType(Object value) {
        return value instanceof String type && !type.isBlank() && InventoryCurio.byIdentifier(type).isEmpty();
    }
}
