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
                            output.accept(TENT_ITEMS.get(color).get());
                        }
                    })
                    .build());

    private static Block bedFor(DyeColor color) {
        return switch (color) {
            case WHITE -> Blocks.WHITE_BED;
            case ORANGE -> Blocks.ORANGE_BED;
            case MAGENTA -> Blocks.MAGENTA_BED;
            case LIGHT_BLUE -> Blocks.LIGHT_BLUE_BED;
            case YELLOW -> Blocks.YELLOW_BED;
            case LIME -> Blocks.LIME_BED;
            case PINK -> Blocks.PINK_BED;
            case GRAY -> Blocks.GRAY_BED;
            case LIGHT_GRAY -> Blocks.LIGHT_GRAY_BED;
            case CYAN -> Blocks.CYAN_BED;
            case PURPLE -> Blocks.PURPLE_BED;
            case BLUE -> Blocks.BLUE_BED;
            case BROWN -> Blocks.BROWN_BED;
            case GREEN -> Blocks.GREEN_BED;
            case RED -> Blocks.RED_BED;
            case BLACK -> Blocks.BLACK_BED;
        };
    }

    public Hlsubstance() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }
}
