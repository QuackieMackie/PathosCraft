package io.github.quackiemackie.pathoscraft.registers;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.FishingRodItem;
import io.github.quackiemackie.pathoscraft.item.PathosItems;

public class PathosItemProperties {
    /**
     * Registers Minecraft Item Properties for items defined in the PathosCraft mod.
     */
    public static void registerMinecraftItemProperties() {
        ItemProperties.register(PathosItems.BASIC_FISHING_ROD.get(),
                ResourceLocation.withDefaultNamespace("cast"),
                (itemStack, clientLevel, entity, seed) -> {
                    if (entity == null) {
                        return 0.0F;
                    } else {
                        boolean flag = entity.getMainHandItem() == itemStack;
                        boolean flag1 = entity.getOffhandItem() == itemStack;
                        if (entity.getMainHandItem().getItem() instanceof FishingRodItem) {
                            flag1 = false;
                        }

                        return (flag || flag1) && entity instanceof Player && ((Player) entity).fishing != null ? 1.0F : 0.0F;
                    }
                }
        );
    }
}
