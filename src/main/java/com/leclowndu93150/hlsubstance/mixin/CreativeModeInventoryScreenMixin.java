package com.leclowndu93150.hlsubstance.mixin;

import com.leclowndu93150.hlsubstance.curios.InventoryCurioSlot;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.CreativeModeInventoryScreen;
import net.minecraft.client.gui.screens.inventory.EffectRenderingInventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.CreativeModeTab;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeModeInventoryScreen.class)
public abstract class CreativeModeInventoryScreenMixin extends EffectRenderingInventoryScreen<CreativeModeInventoryScreen.ItemPickerMenu> {

    @Unique
    private static final int HLSUBSTANCE$OFFHAND_FRAME_U = 34;
    @Unique
    private static final int HLSUBSTANCE$OFFHAND_FRAME_V = 19;

    @Shadow
    private static CreativeModeTab selectedTab;

    protected CreativeModeInventoryScreenMixin(CreativeModeInventoryScreen.ItemPickerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @ModifyArg(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;<init>(Lnet/minecraft/world/inventory/Slot;III)V"), index = 2)
    private int hlsubstance$inventoryCurioSlotX(Slot target, int index, int x, int y) {
        return target instanceof InventoryCurioSlot slot ? slot.curio().creativeX() : x;
    }

    @ModifyArg(method = "selectTab", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screens/inventory/CreativeModeInventoryScreen$SlotWrapper;<init>(Lnet/minecraft/world/inventory/Slot;III)V"), index = 3)
    private int hlsubstance$inventoryCurioSlotY(Slot target, int index, int x, int y) {
        return target instanceof InventoryCurioSlot slot ? slot.curio().creativeY() : y;
    }

    @Inject(method = "renderBg", at = @At("TAIL"))
    private void hlsubstance$renderInventoryCurioSlots(GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY, CallbackInfo callbackInfo) {
        if (selectedTab.getType() != CreativeModeTab.Type.INVENTORY) return;
        for (Slot slot : this.minecraft.player.inventoryMenu.slots) {
            if (slot instanceof InventoryCurioSlot curioSlot && curioSlot.isActive()) {
                guiGraphics.blit(selectedTab.getBackgroundTexture(),
                        this.leftPos + curioSlot.curio().creativeX() - 1, this.topPos + curioSlot.curio().creativeY() - 1,
                        HLSUBSTANCE$OFFHAND_FRAME_U, HLSUBSTANCE$OFFHAND_FRAME_V, 18, 18);
            }
        }
    }
}
