package com.leclowndu93150.hlsubstance;

import com.leclowndu93150.hlsubstance.block.TentBlock;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.Map;

@Mod(Hlsubstance.MODID)
public class Hlsubstance {

    public static final String MODID = "hlsubstance";

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final Map<DyeColor, RegistryObject<Block>> TENT_BLOCKS = new EnumMap<>(DyeColor.class);
    public static final Map<DyeColor, RegistryObject<Item>> TENT_ITEMS = new EnumMap<>(DyeColor.class);

    static {
        for (DyeColor color : DyeColor.values()) {
            String name = color.getName() + "_tent";
            Block vanillaBed = bedFor(color);
            if (vanillaBed == null) continue;
            RegistryObject<Block> block = BLOCKS.register(name,
                    () -> new TentBlock(color, BlockBehaviour.Properties.copy(vanillaBed)
                            .mapColor(state -> state.getValue(TentBlock.PART) == BedPart.FOOT
                                    ? MapColor.WOOL
                                    : color.getMapColor())
                            .pushReaction(PushReaction.DESTROY)));
            RegistryObject<Item> item = ITEMS.register(name,
                    () -> new BedItem(block.get(), new Item.Properties().stacksTo(1)));
            TENT_BLOCKS.put(color, block);
            TENT_ITEMS.put(color, item);
        }
    }

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("tents",
            () -> CreativeModeTab.builder()
                    .withTabsBefore(CreativeModeTabs.COMBAT)
                    .title(Component.translatable("itemGroup.hlsubstance.tents"))
                    .icon(() -> TENT_ITEMS.get(DyeColor.ORANGE).get().getDefaultInstance())
                    .displayItems((params, output) -> {
                        for (DyeColor color : DyeColor.values()) {
                            RegistryObject<Item> item = TENT_ITEMS.get(color);
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

    public Hlsubstance() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
