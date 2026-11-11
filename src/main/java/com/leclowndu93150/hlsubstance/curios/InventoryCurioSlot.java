package com.leclowndu93150.hlsubstance.curios;

import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.ISlotType;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;
import top.theillusivec4.curios.api.type.inventory.ICurioStacksHandler;
import top.theillusivec4.curios.api.type.inventory.IDynamicStackHandler;

import java.util.Optional;

public class InventoryCurioSlot extends Slot {

    private static final Container NO_CONTAINER = new SimpleContainer(0);
    private static final ResourceLocation FALLBACK_ICON = ResourceLocation.fromNamespaceAndPath(CuriosApi.MODID, "slot/empty_curio_slot");

    private final Player player;
    private final InventoryCurio curio;

    public InventoryCurioSlot(Player player, InventoryCurio curio) {
        super(NO_CONTAINER, curio.ordinal(), curio.x(), curio.y());
        this.player = player;
        this.curio = curio;
    }

    public InventoryCurio curio() {
        return curio;
    }

    private Optional<ICuriosItemHandler> inventory() {
        return CuriosApi.getCuriosInventory(player);
    }

    private Optional<ICurioStacksHandler> handler() {
        return inventory()
                .flatMap(inventory -> inventory.getStacksHandler(curio.identifier()))
                .filter(handler -> handler.getSlots() > 0);
    }

    private Optional<IDynamicStackHandler> stacks() {
        return handler().map(ICurioStacksHandler::getStacks);
    }

    @Override
    public ItemStack getItem() {
        return stacks().map(stacks -> stacks.getStackInSlot(0)).orElse(ItemStack.EMPTY);
    }

    @Override
    public void set(ItemStack stack) {
        handler().ifPresent(handler -> {
            ItemStack current = handler.getStacks().getStackInSlot(0);
            handler.getStacks().setStackInSlot(0, stack);
            if (!ItemStack.matches(current, stack) && inventory().map(inventory -> inventory.isSlotActive(curio.identifier(), 0)).orElse(false)) {
                SlotContext context = new SlotContext(curio.identifier(), player, 0, false, handler.getRenders().get(0));
                CuriosApi.getCurio(stack).ifPresent(equipped -> equipped.onEquipFromUse(context));
            }
        });
        setChanged();
    }

    @Override
    public ItemStack remove(int amount) {
        return stacks().map(stacks -> stacks.extractItem(0, amount, false)).orElse(ItemStack.EMPTY);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !stack.isEmpty() && stacks().map(stacks -> stacks.isItemValid(0, stack)).orElse(false);
    }

    @Override
    public boolean mayPickup(Player player) {
        return stacks()
                .map(stacks -> stacks.getStackInSlot(0).isEmpty() || !stacks.extractItem(0, 1, true).isEmpty())
                .orElse(false);
    }

    @Override
    public boolean allowModification(Player player) {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return stacks().map(stacks -> stacks.getSlotLimit(0)).orElse(0);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Math.min(stack.getMaxStackSize(), getMaxStackSize());
    }

    @Override
    public boolean isActive() {
        return handler().isPresent();
    }

    @Override
    public Pair<ResourceLocation, ResourceLocation> getNoItemIcon() {
        ResourceLocation icon = CuriosApi.getSlot(InventoryCuriosConfig.acceptedType(curio), player.level().isClientSide())
                .map(ISlotType::getIcon)
                .orElse(FALLBACK_ICON);
        return Pair.of(InventoryMenu.BLOCK_ATLAS, icon);
    }
}
