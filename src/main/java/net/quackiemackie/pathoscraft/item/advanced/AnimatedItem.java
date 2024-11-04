package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.quackiemackie.pathoscraft.item.client.AnimatedItemRenderer;
import net.quackiemackie.pathoscraft.quest.ModQuests;
import net.quackiemackie.pathoscraft.quest.Quest;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class AnimatedItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public AnimatedItem(Properties properties) {
        super(properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AnimatedItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new AnimatedItemRenderer();

                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public double getTick(Object itemStack) {
        return RenderUtil.getCurrentTick();
    }

//    @Override
//    public InteractionResult useOn(UseOnContext context){
//        Level level = context.getLevel();
//
//
//        if (!level.isClientSide) {
//            ServerLevel serverLevel = (ServerLevel) level;
//            MinecraftServer server = serverLevel.getServer();
//
//            int questId = 1;
//            Quest quest = ModQuests.getQuestById(server, questId);
//
//            if (quest != null) {
//                System.out.println("Quest Id: " + quest.getId());
//                System.out.println("Quest Name: " + quest.getQuestName());
//                System.out.println("Quest Description: " + quest.getQuestDescription());
//                System.out.println("Quest questType: " + quest.getQuestType());
//                System.out.println("Quest questObjective: " + quest.getQuestObjectives());
//                System.out.println("Quest questRewards: " + quest.getQuestRewards());
//            } else {
//                System.out.println("Quest with ID " + questId + " not found!");
//            }
//        }
//        return super.useOn(context);
//    }
}
