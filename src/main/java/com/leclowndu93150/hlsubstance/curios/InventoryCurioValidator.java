package com.leclowndu93150.hlsubstance.curios;

import com.leclowndu93150.hlsubstance.Hlsubstance;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.loading.FMLEnvironment;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.SlotResult;

public final class InventoryCurioValidator {

    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Hlsubstance.MODID, "inventory_curio");

    public static void register(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> CuriosApi.registerCurioPredicate(ID, InventoryCurioValidator::accepts));
    }

    private static boolean accepts(SlotResult result) {
        SlotContext context = result.slotContext();
        LivingEntity entity = context.entity();
        boolean clientSide = entity != null ? entity.level().isClientSide() : FMLEnvironment.dist.isClient();
        return InventoryCurio.byIdentifier(context.identifier())
                .flatMap(curio -> CuriosApi.getSlot(InventoryCuriosConfig.acceptedType(curio), clientSide))
                .map(type -> CuriosApi.testCurioPredicates(type.getValidators(), new SlotResult(
                        new SlotContext(type.getIdentifier(), entity, context.index(), context.cosmetic(), context.visible()),
                        result.stack())))
                .orElse(false);
    }
}
