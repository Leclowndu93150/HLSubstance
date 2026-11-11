package com.leclowndu93150.hlsubstance.mixin;

import com.leclowndu93150.hlsubstance.curios.InventoryCurioSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin extends EffectRenderingInventoryScreen<InventoryMenu> {

    @Unique
    private static final int HLSUBSTANCE$OFFHAND_FRAME_U = 76;
    @Unique
    private static final int HLSUBSTANCE$OFFHAND_FRAME_V = 61;

    protected InventoryScreenMixin(InventoryMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void hlsubstance$renderInventoryCurioSlots(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        for (Slot slot : this.menu.slots) {
            if (slot instanceof InventoryCurioSlot && slot.isActive()) {
                guiGraphics.blit(INVENTORY_LOCATION, this.leftPos + slot.x - 1, this.topPos + slot.y - 1,
                        HLSUBSTANCE$OFFHAND_FRAME_U, HLSUBSTANCE$OFFHAND_FRAME_V, 18, 18);
            }
        }
    }
}
