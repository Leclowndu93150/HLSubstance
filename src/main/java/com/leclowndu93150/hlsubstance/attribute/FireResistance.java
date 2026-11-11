package com.leclowndu93150.hlsubstance.attribute;

import com.leclowndu93150.hlsubstance.Hlsubstance;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = Hlsubstance.MODID)
public final class FireResistance {

    public static void addToEntities(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, Hlsubstance.FIRE_RESISTANCE);
        }
    }

    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        if (!event.getSource().is(DamageTypeTags.IS_FIRE)) return;
        float amount = event.getAmount() * (float) (1.0 - event.getEntity().getAttributeValue(Hlsubstance.FIRE_RESISTANCE));
        if (amount <= 0.0F) {
            event.setCanceled(true);
        } else {
            event.setAmount(amount);
        }
    }
}
