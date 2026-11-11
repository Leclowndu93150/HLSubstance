package com.leclowndu93150.hlsubstance.attribute;

import com.leclowndu93150.hlsubstance.Hlsubstance;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Hlsubstance.MODID)
public final class FireResistance {

    public static void addToEntities(EntityAttributeModificationEvent event) {
        for (EntityType<? extends LivingEntity> type : event.getTypes()) {
            event.add(type, Hlsubstance.FIRE_RESISTANCE.get());
        }
    }

    @SubscribeEvent
    public static void onAttack(LivingAttackEvent event) {
        if (!event.getSource().is(DamageTypeTags.IS_FIRE)) return;
        if (event.getEntity().getAttributeValue(Hlsubstance.FIRE_RESISTANCE.get()) >= 1.0) {
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onHurt(LivingHurtEvent event) {
        if (!event.getSource().is(DamageTypeTags.IS_FIRE)) return;
        float amount = event.getAmount() * (float) (1.0 - event.getEntity().getAttributeValue(Hlsubstance.FIRE_RESISTANCE.get()));
        if (amount <= 0.0F) {
            event.setCanceled(true);
        } else {
            event.setAmount(amount);
        }
    }
}
