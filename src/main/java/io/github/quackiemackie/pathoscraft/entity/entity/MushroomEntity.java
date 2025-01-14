package io.github.quackiemackie.pathoscraft.entity.entity;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import io.github.quackiemackie.pathoscraft.entity.PathosEntities;
import io.github.quackiemackie.pathoscraft.registers.PathosTags;
import org.jetbrains.annotations.Nullable;

public class MushroomEntity extends TamableAnimal {

    public final AnimationState sitAnimationState = new AnimationState();
    public final AnimationState standAnimationState = new AnimationState();
    private int sitAnimationTimeout = 0;
    private int standAnimationTimeout = 0;
    public boolean isSitting = false;
    private boolean isStanding = false;

    public MushroomEntity(EntityType<MushroomEntity> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH,  10D)
                .add(Attributes.FOLLOW_RANGE, 10D)
                .add(Attributes.MOVEMENT_SPEED, 1D);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0D, (stack) -> stack.is(PathosTags.EntityFood.MUSHROOM_FOOD), false));

        if (!this.isTame()) {
            this.goalSelector.addGoal(1, new PanicGoal(this, 1.25D));
            this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.0D));
            this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 1.0D));
            this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 8.0F));
            this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
            this.goalSelector.addGoal(9, new RandomStrollGoal(this, 0.4D));
        }

        if (this.isTame()) {
            this.goalSelector.addGoal(4, new FollowOwnerGoal(this, 2.0D, 24.0F, 24.0F));
            this.goalSelector.addGoal(9, new RandomStrollGoal(this, 0.2D));
        }
    }

    @Override
    public boolean isFood(ItemStack itemStack) {
        return itemStack.is(PathosTags.EntityFood.MUSHROOM_FOOD);
    }

    @Nullable
    @Override
    public MushroomEntity getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        MushroomEntity mushroomEntity = PathosEntities.MUSHROOM.get().create(level);

        if (this.isTame()) {
            mushroomEntity.setOwnerUUID(this.getOwnerUUID());
            mushroomEntity.setTame(true, true);
        }

        return mushroomEntity;
    }

    @Override
    public void setOrderedToSit(boolean isSitting) {
        super.setOrderedToSit(isSitting);

        if (isSitting) {
            for (int i = 0; i < 5; i++) {
                double dx = this.getRandom().nextGaussian() * 0.02D;
                double dy = this.getRandom().nextGaussian() * 0.02D;
                double dz = this.getRandom().nextGaussian() * 0.02D;

                double offsetX = this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();
                double offsetY = 0.5D + this.getRandom().nextFloat() * this.getBbHeight();
                double offsetZ = this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();

                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, dx, dy, dz);
            }
            this.setupSitAnimationState(isSitting);
        } else {
            for (int i = 0; i < 5; i++) {
                double dx = this.getRandom().nextGaussian() * 0.02D;
                double dy = this.getRandom().nextGaussian() * 0.02D;
                double dz = this.getRandom().nextGaussian() * 0.02D;

                double offsetX = this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();
                double offsetY = 0.5D + this.getRandom().nextFloat() * this.getBbHeight();
                double offsetZ = this.getRandom().nextFloat() * this.getBbWidth() * 2.0F - this.getBbWidth();

                this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getX() + offsetX, this.getY() + offsetY, this.getZ() + offsetZ, dx, dy, dz);
            }
            this.setupStandAnimationState(isSitting);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);

        if (!this.isTame() && itemStack.is(PathosTags.EntityFood.MUSHROOM_TAME_FOOD)) {
            return this.handleTaming(player, itemStack);
        }

        if (this.isOwnedBy(player) && this.isTame() && itemStack.isEmpty()) {
            return this.handleSittingToggle();
        }

        return super.mobInteract(player, hand);
    }

    private InteractionResult handleTaming(Player player, ItemStack itemStack) {
        if (!player.isCreative()) {
            itemStack.shrink(1);
        }

        if (!this.level().isClientSide) {
            if (this.random.nextInt(3) == 0) { // 33% chance to tame
                this.tame(player);
                this.level().broadcastEntityEvent(this, (byte) 7);
            } else {
                this.level().broadcastEntityEvent(this, (byte) 6);
            }
        }

        return InteractionResult.SUCCESS;
    }

    private InteractionResult handleSittingToggle() {
        this.setOrderedToSit(!this.isOrderedToSit());
        return InteractionResult.SUCCESS;
    }

    private void setupSitAnimationState(Boolean isSitting) {
        if (!this.sitAnimationState.isStarted()) {
            this.sitAnimationState.start(this.tickCount);

            if (this.standAnimationState.isStarted()) {
                this.standAnimationState.stop();
            }

            this.sitAnimationTimeout = 30;
            this.isStanding = !isSitting;
            this.isSitting = isSitting;
        }

        if (this.sitAnimationTimeout > 0) {
            --this.sitAnimationTimeout;
        } else if (this.sitAnimationState.isStarted()) {
            this.sitAnimationState.stop();
        }
    }

    private void setupStandAnimationState(Boolean isSitting) {
        if (!this.standAnimationState.isStarted()) {
            this.standAnimationState.start(this.tickCount);

            if (this.sitAnimationState.isStarted()) {
                this.sitAnimationState.stop();
            }

            this.standAnimationTimeout = 30;
            this.isStanding = isSitting;
            this.isSitting = !isSitting;
        }

        if (this.standAnimationTimeout > 0) {
            --this.standAnimationTimeout;
        } else if (this.standAnimationState.isStarted()) {
            this.standAnimationState.stop();
        }
    }

    @Override
    public void tick() {
        super.tick();

        if (this.isSitting && !this.isStanding) {
            this.getNavigation().stop();
            this.setDeltaMovement(0, 0, 0);
        }
    }

    @Override
    public void tame(Player player) {
        super.tame(player);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag tag) {
        super.addAdditionalSaveData(tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag tag) {
        super.readAdditionalSaveData(tag);
    }
}
