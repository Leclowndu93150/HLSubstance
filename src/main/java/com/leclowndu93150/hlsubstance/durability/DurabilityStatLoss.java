package com.leclowndu93150.hlsubstance.durability;

import com.leclowndu93150.hlsubstance.Hlsubstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.List;

@EventBusSubscriber(modid = Hlsubstance.MODID)
public final class DurabilityStatLoss {

    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        double loss = DurabilityConfig.statLoss(event.getItemStack());
        if (loss <= 0.0) return;
        for (ItemAttributeModifiers.Entry entry : List.copyOf(event.getModifiers())) {
            if (!DurabilityConfig.affects(entry.attribute())) continue;
            AttributeModifier modifier = entry.modifier();
            double amount = modifier.amount() - Math.abs(modifier.amount()) * loss;
            event.replaceModifier(entry.attribute(), new AttributeModifier(modifier.id(), amount, modifier.operation()), entry.slot());
        }
    }

    @SubscribeEvent
    public static void onBreakSpeed(PlayerEvent.BreakSpeed event) {
        if (!DurabilityConfig.affectMiningSpeed()) return;
        ItemStack stack = event.getEntity().getMainHandItem();
        double loss = DurabilityConfig.statLoss(stack);
        if (loss <= 0.0 || stack.getDestroySpeed(event.getState()) <= 1.0F) return;
        event.setNewSpeed((float) (event.getNewSpeed() * (1.0 - loss)));
    }
}
