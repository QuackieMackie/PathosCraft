package net.quackiemackie.pathoscraft.entity.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WallClimberNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.quackiemackie.pathoscraft.registers.PathosTags;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class SpiderMountEntity extends AbstractHorse {
    private static final EntityDataAccessor<Optional<UUID>> OWNER_UUID = SynchedEntityData.defineId(SpiderMountEntity.class, EntityDataSerializers.OPTIONAL_UUID);
    private static final EntityDataAccessor<Byte> DATA_FLAGS_ID = SynchedEntityData.defineId(SpiderMountEntity.class, EntityDataSerializers.BYTE);

    public final AnimationState damagedAnimationState = new AnimationState();
    public final AnimationState eatAnimationState = new AnimationState();
    private int eatAnimationTimeout = 0;
    private boolean isEating = false;

    public SpiderMountEntity(EntityType<? extends SpiderMountEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void registerGoals() {
        Predicate<ItemStack> temptationItems = (stack) -> stack.is(PathosTags.EntityFood.SPIDER_MOUNT_FOOD);
        this.goalSelector.addGoal(1, new FloatGoal(this));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, temptationItems, false));
        this.goalSelector.addGoal(3, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(4, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(5, new RandomStrollGoal(this, 0.8D));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Animal.createLivingAttributes()
                .add(Attributes.MAX_HEALTH, 50.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.25D)
                .add(Attributes.JUMP_STRENGTH, 0.7D)
                .add(Attributes.FOLLOW_RANGE, 32.0D);
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(OWNER_UUID, Optional.empty());
        builder.define(DATA_FLAGS_ID, (byte) 0);
    }

    private void handleEatingAnimation() {
        if (this.isEating && this.level().isClientSide) {
            if (this.eatAnimationTimeout <= 0) {
                this.eatAnimationTimeout = 11;
                this.eatAnimationState.start(this.tickCount);
            } else {
                --this.eatAnimationTimeout;
            }
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide) {
            this.handleEatingAnimation();
        }
        if (!this.level().isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.eatAnimationTimeout <= 0) {
            this.isEating = false;
        }
    }

    @Override
    public void handleDamageEvent(DamageSource damageSource) {
        this.damagedAnimationState.start(this.tickCount);
        super.handleDamageEvent(damageSource);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && handleEating(player, itemStack)) {
            this.setEating(true);
            if (!player.getAbilities().instabuild) {
                itemStack.shrink(1);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        if (!this.level().isClientSide && !player.isShiftKeyDown()) {
            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level().isClientSide());
        }
        return InteractionResult.PASS;
    }

    protected boolean handleEating(Player player, ItemStack stack) {
        float healAmount = 0.0F;
        if (stack.is(Items.CHICKEN) || stack.is(Items.PORKCHOP) || stack.is(Items.BEEF) || stack.is(Items.MUTTON) || stack.is(Items.RABBIT)) {
            healAmount = 2.0F;
        } else if (stack.is(Items.COOKED_CHICKEN) || stack.is(Items.COOKED_PORKCHOP) || stack.is(Items.COOKED_BEEF) || stack.is(Items.COOKED_MUTTON) || stack.is(Items.COOKED_RABBIT)) {
            healAmount = 4.0F;
        }
        if (this.getHealth() < this.getMaxHealth() && healAmount > 0.0F) {
            this.heal(healAmount);
            this.isEating = true;
            return true;
        }
        return false;
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new WallClimberNavigation(this, level);
    }

    public boolean isClimbing() {
        return (this.entityData.get(DATA_FLAGS_ID) & 1) != 0;
    }

    public void setClimbing(boolean climbing) {
        byte b0 = this.entityData.get(DATA_FLAGS_ID);
        if (climbing) {
            b0 = (byte) (b0 | 1);
        } else {
            b0 = (byte) (b0 & -2);
        }
        this.entityData.set(DATA_FLAGS_ID, b0);
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    protected void doPlayerRide(@NotNull Player player) {
        if (!this.level().isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Override
    public boolean canJump() {
        return true;
    }

    @Override
    protected void tickRidden(@NotNull Player player, @NotNull Vec3 travelVector) {
        super.tickRidden(player, travelVector);
        Vec2 vec2 = this.getRiddenRotation(player);
        this.setRot(vec2.y, vec2.x);
        this.yRotO = this.yBodyRot = this.yHeadRot = this.getYRot();
        if (this.isControlledByLocalInstance()) {
            if (travelVector.z <= 0.0) {
                this.gallopSoundCounter = 0;
            }
            if (this.onGround()) {
                this.setIsJumping(false);
            }
            this.setClimbing(this.horizontalCollision);
            if (this.isClimbing()) {
                this.setDeltaMovement(this.getDeltaMovement().add(0.0, 0.2, 0.0));
            }
        }
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(Player player, @NotNull Vec3 travelVector) {
        float f = player.xxa * 0.5F;
        float f1 = player.zza;
        if (f1 <= 0.0F) {
            f1 *= 0.25F;
        }
        return new Vec3(f, 0.0, f1);
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player player) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        if (jumpPower < 0) {
            jumpPower = 0;
        } else {
            this.allowStandSliding = true;
            this.standIfPossible();
        }
        if (jumpPower >= 90) {
            this.playerJumpPendingScale = 1.0F;
        } else {
            this.playerJumpPendingScale = 0.4F + 0.4F * (float) jumpPower / 90.0F;
        }
    }

    @Override
    public void handleStartJump(int jumpPower) {
        this.allowStandSliding = true;
        this.standIfPossible();
        this.playJumpSound();
    }

    @Override
    public void handleStopJump() {
        this.setIsJumping(false);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof Player) {
            return (Player) entity;
        }
        return super.getControllingPassenger();
    }

    @Override
    protected void positionRider(@NotNull Entity passenger, Entity.@NotNull MoveFunction callback) {
        super.positionRider(passenger, callback);
        if (passenger instanceof LivingEntity) {
            ((LivingEntity) passenger).yBodyRot = this.yBodyRot;
        }
    }

    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
        double d0 = this.getX() + direction.x;
        double d1 = this.getBoundingBox().minY;
        double d2 = this.getZ() + direction.z;
        BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos();
        for (Pose pose : passenger.getDismountPoses()) {
            blockPos.set(d0, d1, d2);
            double d3 = this.getBoundingBox().maxY + 0.75;
            do {
                double d4 = this.level().getBlockFloorHeight(blockPos);
                if (blockPos.getY() + d4 > d3) {
                    break;
                }
                if (DismountHelper.isBlockFloorValid(d4)) {
                    AABB aabb = passenger.getLocalBoundsForPose(pose);
                    Vec3 vec3 = new Vec3(d0, blockPos.getY() + d4, d2);
                    if (DismountHelper.canDismountTo(this.level(), passenger, aabb.move(vec3))) {
                        passenger.setPose(pose);
                        return vec3;
                    }
                }
                blockPos.move(Direction.UP);
            } while (blockPos.getY() < d3);
        }
        return null;
    }

    @Override
    public @NotNull Vec3 getDismountLocationForPassenger(LivingEntity livingEntity) {
        Vec3 vec3 = getCollisionHorizontalEscapeVector(
                this.getBbWidth(), livingEntity.getBbWidth(), this.getYRot() + (livingEntity.getMainArm() == HumanoidArm.RIGHT ? 90.0F : -90.0F)
        );
        Vec3 vec3Direction = this.getDismountLocationInDirection(vec3, livingEntity);
        if (vec3Direction != null) {
            return vec3Direction;
        } else {
            vec3 = getCollisionHorizontalEscapeVector(
                    this.getBbWidth(), livingEntity.getBbWidth(), this.getYRot() + (livingEntity.getMainArm() == HumanoidArm.LEFT ? 90.0F : -90.0F)
            );
            vec3Direction = this.getDismountLocationInDirection(vec3, livingEntity);
            return vec3Direction != null ? vec3Direction : this.position();
        }
    }
}