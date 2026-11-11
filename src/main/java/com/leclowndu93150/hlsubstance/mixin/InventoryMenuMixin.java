package com.leclowndu93150.hlsubstance.mixin;

import com.leclowndu93150.hlsubstance.curios.InventoryCurio;
import com.leclowndu93150.hlsubstance.curios.InventoryCurioSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.RecipeBookMenu;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CraftingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryMenu.class)
public abstract class InventoryMenuMixin extends RecipeBookMenu<CraftingInput, CraftingRecipe> {

    protected InventoryMenuMixin(MenuType<?> menuType, int containerId) {
        super(menuType, containerId);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void hlsubstance$addInventoryCurioSlots(Inventory inventory, boolean active, Player owner, CallbackInfo callbackInfo) {
        for (InventoryCurio curio : InventoryCurio.values()) {
            this.addSlot(new InventoryCurioSlot(owner, curio));
        }
    }
}
