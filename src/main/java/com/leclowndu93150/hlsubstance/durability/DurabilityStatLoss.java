package com.leclowndu93150.hlsubstance.durability;

import com.leclowndu93150.hlsubstance.Hlsubstance;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;

@Mod.EventBusSubscriber(modid = Hlsubstance.MODID)
public final class DurabilityStatLoss {

    @SubscribeEvent
    public static void onItemAttributeModifiers(ItemAttributeModifierEvent event) {
        double loss = DurabilityConfig.statLoss(event.getItemStack());
        if (loss <= 0.0) return;
        for (Map.Entry<Attribute, AttributeModifier> entry : List.copyOf(event.getModifiers().entries())) {
            if (!DurabilityConfig.affects(entry.getKey())) continue;
            AttributeModifier modifier = entry.getValue();
            double amount = modifier.getAmount() - Math.abs(modifier.getAmount()) * loss;
            event.removeModifier(entry.getKey(), modifier);
            event.addModifier(entry.getKey(), new AttributeModifier(modifier.getId(), modifier.getName(), amount, modifier.getOperation()));
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
