package com.leclowndu93150.hlsubstance.durability;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public final class DurabilityConfig {

    private static final Pattern LOSS_STEP_PATTERN = Pattern.compile("\\d+(\\.\\d+)?=\\d+(\\.\\d+)?");

    public static final ModConfigSpec SPEC;

    private static final ModConfigSpec.BooleanValue HARDNESS_SCALING_ENABLED;
    private static final ModConfigSpec.DoubleValue HARDNESS_MULTIPLIER;
    private static final ModConfigSpec.BooleanValue STAT_LOSS_ENABLED;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> STAT_LOSS_STEPS;
    private static final ModConfigSpec.ConfigValue<List<? extends String>> AFFECTED_ATTRIBUTES;
    private static final ModConfigSpec.BooleanValue AFFECT_MINING_SPEED;

    private static boolean hardnessScalingEnabled;
    private static double hardnessMultiplier;
    private static boolean statLossEnabled;
    private static List<LossStep> lossSteps = List.of();
    private static Set<ResourceLocation> affectedAttributes = Set.of();
    private static boolean affectMiningSpeed;

    static {
        ModConfigSpec.Builder builder = new ModConfigSpec.Builder();

        builder.push("hardnessDurability");
        HARDNESS_SCALING_ENABLED = builder
                .comment("Scale the durability a tool loses when breaking a block with the hardness of that block.")
                .define("enabled", true);
        HARDNESS_MULTIPLIER = builder
                .comment("Durability lost per block = block hardness x this multiplier, rounded, never below 1.",
                        "With 2.5: netherrack (0.4) costs 1, stone (1.5) costs 4, deepslate (3.0) costs 8, obsidian (50.0) costs 125.")
                .defineInRange("multiplier", 2.5, 0.0, 1000.0);
        builder.pop();

        builder.push("durabilityStatLoss");
        STAT_LOSS_ENABLED = builder
                .comment("Lower the stats of equipment as it loses durability.")
                .define("enabled", true);
        STAT_LOSS_STEPS = builder
                .comment("Format: \"remainingDurabilityPercent=statLossPercent\".",
                        "\"50=10\" means: at 50% durability left or less, the stats are lowered by 10%.",
                        "The entry with the lowest matching durability percent wins.")
                .defineListAllowEmpty("steps", List.of("50=10", "25=25", "10=50"), () -> "50=10",
                        value -> value instanceof String step && LOSS_STEP_PATTERN.matcher(step).matches());
        AFFECTED_ATTRIBUTES = builder
                .comment("Attributes granted by the equipment that are lowered.",
                        "Bonuses shrink towards 0, penalties (like the attack speed of a sword) get bigger.")
                .defineListAllowEmpty("attributes", List.of(
                                "minecraft:generic.attack_damage",
                                "minecraft:generic.armor",
                                "minecraft:generic.armor_toughness",
                                "minecraft:generic.knockback_resistance"), () -> "minecraft:generic.attack_damage",
                        value -> value instanceof String id && ResourceLocation.tryParse(id) != null);
        AFFECT_MINING_SPEED = builder
                .comment("Also lower the mining speed of tools.")
                .define("miningSpeed", true);
        builder.pop();

        SPEC = builder.build();
    }

    public static void onLoad(ModConfigEvent.Loading event) {
        refresh();
    }

    public static void onReload(ModConfigEvent.Reloading event) {
        refresh();
    }

    private static void refresh() {
        hardnessScalingEnabled = HARDNESS_SCALING_ENABLED.get();
        hardnessMultiplier = HARDNESS_MULTIPLIER.get();
        statLossEnabled = STAT_LOSS_ENABLED.get();
        affectMiningSpeed = AFFECT_MINING_SPEED.get();

        List<LossStep> steps = new ArrayList<>();
        for (String step : STAT_LOSS_STEPS.get()) {
            String[] parts = step.split("=");
            steps.add(new LossStep(Double.parseDouble(parts[0]), Math.min(1.0, Double.parseDouble(parts[1]) / 100.0)));
        }
        steps.sort(Comparator.comparingDouble(LossStep::durabilityPercent));
        lossSteps = steps;

        Set<ResourceLocation> attributes = new HashSet<>();
        for (String id : AFFECTED_ATTRIBUTES.get()) {
            attributes.add(ResourceLocation.parse(id));
        }
        affectedAttributes = attributes;
    }

    public static boolean hardnessScalingEnabled() {
        return hardnessScalingEnabled;
    }

    public static double hardnessMultiplier() {
        return hardnessMultiplier;
    }

    public static boolean affectMiningSpeed() {
        return affectMiningSpeed;
    }

    public static boolean affects(Holder<Attribute> attribute) {
        return attribute.unwrapKey().map(key -> affectedAttributes.contains(key.location())).orElse(false);
    }

    public static double statLoss(ItemStack stack) {
        if (!statLossEnabled || !stack.isDamageableItem() || !stack.isDamaged()) return 0.0;
        int maxDamage = stack.getMaxDamage();
        double remainingPercent = 100.0 * (maxDamage - stack.getDamageValue()) / maxDamage;
        for (LossStep step : lossSteps) {
            if (remainingPercent <= step.durabilityPercent()) return step.loss();
        }
        return 0.0;
    }

    private record LossStep(double durabilityPercent, double loss) {
    }
}
