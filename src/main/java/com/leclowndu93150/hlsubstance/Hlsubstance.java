package com.leclowndu93150.hlsubstance;

import com.leclowndu93150.hlsubstance.attribute.FireResistance;
import com.leclowndu93150.hlsubstance.block.TentBlock;
import com.leclowndu93150.hlsubstance.curios.InventoryCurioValidator;
import com.leclowndu93150.hlsubstance.curios.InventoryCuriosConfig;
import com.leclowndu93150.hlsubstance.durability.DurabilityConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.minecraft.world.item.BedItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BedPart;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.Map;

@Mod(Hlsubstance.MODID)
public class Hlsubstance {

    public static final String MODID = "hlsubstance";

    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);
    public static final DeferredRegister<Attribute> ATTRIBUTES = DeferredRegister.create(Registries.ATTRIBUTE, MODID);

    public static final DeferredHolder<Attribute, Attribute> FIRE_RESISTANCE = ATTRIBUTES.register("fire_resistance",
            () -> new RangedAttribute("attribute.name.hlsubstance.fire_resistance", 0.0, 0.0, 1.0));

    public static final Map<DyeColor, DeferredBlock<Block>> TENT_BLOCKS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, DeferredItem<Item>> TENT_ITEMS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_tent";
            Block vanillaBed = bedFor(color);
            if (vanillaBed == null) continue;
            DeferredBlock<Block> block = BLOCKS.register(name,
                    () -> new TentBlock(color, BlockBehaviour.Properties.ofFullCopy(vanillaBed)
                            .mapColor(state -> state.getValue(TentBlock.PART) == BedPart.FOOT
                                    ? MapColor.WOOL
                                    : color.getMapColor())
                            .pushReaction(PushReaction.DESTROY)));
            DeferredItem<Item> item = ITEMS.register(name,
                    () -> new BedItem(block.get(), new Item.Properties().stacksTo(1)));
            TENT_BLOCKS.put(color, block);
            TENT_ITEMS.put(color, item);
        }
    }

    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tents",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.hlsubstance.tents"))
                    .icon(() -> TENT_ITEMS.get(DyeColor.ORANGE).get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        for (DyeColor color : DyeColor.values()) {
                            DeferredItem<Item> item = TENT_ITEMS.get(color);
                            if (item != null) output.accept(item.get());
                        }
                    })
                    .build());

    private static Block bedFor(DyeColor color) {
        return switch (color.getId()) {
            case 0 -> Blocks.WHITE_BED;
            case 1 -> Blocks.ORANGE_BED;
            case 2 -> Blocks.MAGENTA_BED;
            case 3 -> Blocks.LIGHT_BLUE_BED;
            case 4 -> Blocks.YELLOW_BED;
            case 5 -> Blocks.LIME_BED;
            case 6 -> Blocks.PINK_BED;
            case 7 -> Blocks.GRAY_BED;
            case 8 -> Blocks.LIGHT_GRAY_BED;
            case 9 -> Blocks.CYAN_BED;
            case 10 -> Blocks.PURPLE_BED;
            case 11 -> Blocks.BLUE_BED;
            case 12 -> Blocks.BROWN_BED;
            case 13 -> Blocks.GREEN_BED;
            case 14 -> Blocks.RED_BED;
            case 15 -> Blocks.BLACK_BED;
            default -> null;
        };
    }

    public Hlsubstance(IEventBus modEventBus, ModContainer modContainer) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
        modContainer.registerConfig(ModConfig.Type.COMMON, DurabilityConfig.SPEC);
        modEventBus.addListener(DurabilityConfig::onLoad);
        modEventBus.addListener(DurabilityConfig::onReload);
        ATTRIBUTES.register(modEventBus);
        modEventBus.addListener(FireResistance::addToEntities);
        modContainer.registerConfig(ModConfig.Type.SERVER, InventoryCuriosConfig.SPEC);
        modEventBus.addListener(InventoryCuriosConfig::onLoad);
        modEventBus.addListener(InventoryCuriosConfig::onReload);
        modEventBus.addListener(InventoryCurioValidator::register);
    }
}
