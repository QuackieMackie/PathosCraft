package net.quackiemackie.pathoscraft.item.advanced;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.item.client.AnimatedItemRenderer;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.handlers.QuestHandler;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;
import java.util.logging.Logger;

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

    @Override
    public InteractionResult useOn(UseOnContext context){
        Player player = context.getPlayer();
        Level world = context.getLevel();

        if (!world.isClientSide && player != null) {
            QuestHandler questHandler = PathosCraft.getQuestHandler();
            if (questHandler != null) {
                for (Quest quest : questHandler.getQuests()) {
                    String questInfo = "Quest ID: " + quest.getId()
                            + ", Name: " + quest.getQuestName()
                            + ", Description: " + quest.getQuestDescription();

                    player.sendSystemMessage(Component.literal(questInfo));
                    Logger.getLogger(PathosCraft.MOD_ID).info(questInfo);
                }

                Quest questId = questHandler.getQuestById(1);
                String questInfo = "Quest ID: " + questId.getId()
                        + ", Name: " + questId.getQuestName()
                        + ", Description: " + questId.getQuestDescription()
                        + ", Objective: " + questId.getQuestObjectives()
                        + ", Reward: " + questId.getQuestRewards();
                Logger.getLogger(PathosCraft.MOD_ID).info(questInfo);
            } else {
                player.sendSystemMessage(Component.literal("QuestHandler is not available"));
            }
        }

        return super.useOn(context);
    }
}
