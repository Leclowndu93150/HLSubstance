package com.leclowndu93150.hlsubstance.mixin;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.SmithingTemplateItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin extends ItemCombinerMenu {
    protected SmithingMenuMixin(MenuType<?> menuType, int containerId, Inventory inventory, ContainerLevelAccess access) {
        super(menuType, containerId, inventory, access);
    }

    @Inject(method = "shrinkStackInSlot", at = @At("HEAD"), cancellable = true)
    private void infinitetemplates$keepSmithingTemplates(int slot, CallbackInfo callbackInfo) {
        if (slot == SmithingMenu.TEMPLATE_SLOT && this.inputSlots.getItem(slot).getItem() instanceof SmithingTemplateItem) {
            callbackInfo.cancel();
        }
    }
}
