package io.github.quackiemackie.pathoscraft.item.items.misc;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import io.github.quackiemackie.pathoscraft.util.handlers.abilities.AstralFormHandler;
import io.github.quackiemackie.pathoscraft.item.renderer.AstralLanternRenderer;
import io.github.quackiemackie.pathoscraft.registers.PathosAttachments;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.animation.*;
import software.bernie.geckolib.util.RenderUtil;

import java.util.function.Consumer;

public class AstralLantern extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public AstralLantern(Properties properties) {
        super(properties);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private AstralLanternRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getGeoItemRenderer() {
                if (this.renderer == null)
                    this.renderer = new AstralLanternRenderer();

                return this.renderer;
            }
        });
    }

    private PlayState predicate(AnimationState animationState) {
        animationState.getController().setAnimation(RawAnimation.begin().then("blank", Animation.LoopType.PLAY_ONCE));
        return PlayState.STOP;
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
        Level level = context.getLevel();
        Player player = context.getPlayer();

        if (!level.isClientSide && !player.getData(PathosAttachments.IN_ASTRAL_FORM)) {
            AstralFormHandler.enterAstralForm(player);

            context.getItemInHand().hurtAndBreak(1, ((ServerLevel) level), context.getPlayer(), item ->
                    context.getPlayer().onEquippedItemBroken(item, EquipmentSlot.MAINHAND));

            level.playSound(null, context.getClickedPos(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.NEUTRAL);
            ((ServerLevel) level).sendParticles(ParticleTypes.PORTAL, player.getX(), player.getY(), player.getZ(), 50, 0.5, 0.5, 0.5, 0.2);
        }

        return super.useOn(context);
    }
}
