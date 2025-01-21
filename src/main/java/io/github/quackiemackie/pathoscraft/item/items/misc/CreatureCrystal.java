package io.github.quackiemackie.pathoscraft.item.items.misc;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.registers.PathosDataComponents;

import java.util.List;
import java.util.UUID;

public class CreatureCrystal extends Item {

    public CreatureCrystal(Properties properties) {
        super(properties);
    }

    /**
     * Handles logic when right-clicking on a block (USE_ON behavior).
     */
    public static void captureEntity(ItemStack stack, Player player, Entity target) {
        // Save the entity's data
        CompoundTag entityData = new CompoundTag();
        target.save(entityData);

        // Store the entity's UUID to avoid duplication issues
        entityData.putUUID("CapturedEntityUUID", target.getUUID());

        // Store the data into the crystal
        stack.set(PathosDataComponents.ENTITY_CAPTURED_DATA, entityData);

        // Notify the player
        PathosCraft.LOGGER.info("Entity captured! Data: {}", entityData);
        player.displayClientMessage(Component.literal("Captured: " + target.getDisplayName().getString()), true);

        // Remove the entity from the world
        target.discard();
    }

    /**
     * Releases an entity at a specific BlockPos.
     */
    public InteractionResult releaseEntityAt(ItemStack stack, Level level, Player player, BlockPos blockPos) {
        CompoundTag entityData = stack.get(PathosDataComponents.ENTITY_CAPTURED_DATA);

        if (entityData == null || entityData.isEmpty()) {
            player.displayClientMessage(Component.literal("No captured entity to release!"), true);
            return InteractionResult.FAIL;
        }

        // Get the captured entity type
        String entityTypeString = entityData.getString("id");
        EntityType<?> entityType = EntityType.byString(entityTypeString).orElse(null);
        if (entityType == null) {
            player.displayClientMessage(Component.literal("Could not release entity, type is invalid!"), true);
            PathosCraft.LOGGER.error("Invalid entity type: {}", entityTypeString);
            return InteractionResult.FAIL;
        }

        // Create the new entity
        Entity entity = entityType.create(level);
        if (entity == null) {
            player.displayClientMessage(Component.literal("Could not release entity! Entity creation failed."), true);
            PathosCraft.LOGGER.error("Failed to create entity of type: {}", entityTypeString);
            return InteractionResult.FAIL;
        }

        // Load and position the entity
        entity.load(entityData);
        entity.setUUID(UUID.randomUUID());
        entity.setPos(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5); // Center on the block

        // Add entity to the world
        if (level.addFreshEntity(entity)) {
            stack.remove(PathosDataComponents.ENTITY_CAPTURED_DATA);
            player.displayClientMessage(Component.literal("Released: " + entity.getDisplayName().getString()), true);
            PathosCraft.LOGGER.info("Entity released at block position: {}", blockPos);
            return InteractionResult.SUCCESS;
        } else {
            player.displayClientMessage(Component.literal("Failed to release entity at the target position!"), true);
            return InteractionResult.FAIL;
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        CompoundTag capturedData = stack.get(PathosDataComponents.ENTITY_CAPTURED_DATA);

        if (capturedData == null || capturedData.isEmpty()) {
            tooltipComponents.add(Component.literal("§eRight-click §7a creature to pick it up."));
        } else if (!tooltipFlag.isAdvanced() || !tooltipFlag.hasShiftDown()) {
            tooltipComponents.add(Component.literal("§7Hold §eShift §7for more information."));
            tooltipComponents.add(Component.literal("§eShift + Right-click §7a block to place the captured creature."));
        } else {
            addEntityDetailsTooltip(capturedData, tooltipComponents);
        }
    }

    /**
     * Adds detailed information about the captured entity to the tooltip.
     *
     * @param capturedData      The NBT data of the captured entity.
     * @param tooltipComponents The list of tooltip components to append to.
     */
    private void addEntityDetailsTooltip(CompoundTag capturedData, List<Component> tooltipComponents) {
        if (capturedData.contains("CustomName")) {
            tooltipComponents.add(Component.literal("§7Name: §a" + capturedData.getString("CustomName")));
        }

        String entityType = capturedData.getString("id");
        String trimmedEntityType = entityType.substring(entityType.lastIndexOf(":") + 1);
        tooltipComponents.add(Component.literal("§7Type: §a" + trimmedEntityType));

        if (capturedData.contains("Health")) {
            tooltipComponents.add(Component.literal("§7Health: §a" + capturedData.getFloat("Health")));
        }
    }
}