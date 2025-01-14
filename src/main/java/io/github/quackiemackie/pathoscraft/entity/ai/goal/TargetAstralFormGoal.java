package io.github.quackiemackie.pathoscraft.entity.ai.goal;

import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.AABB;
import io.github.quackiemackie.pathoscraft.entity.entity.AstralFormEntity;

import java.util.List;

public class TargetAstralFormGoal extends TargetGoal {
    private final Monster mob;
    private AstralFormEntity targetEntity;

    public TargetAstralFormGoal(Monster mob) {
        super(mob, true, true);
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        double radius = 10.0D;
        AABB boundingBox = new AABB(
                this.mob.getX() - radius, this.mob.getY() - radius, this.mob.getZ() - radius,
                this.mob.getX() + radius, this.mob.getY() + radius, this.mob.getZ() + radius
        );

        List<AstralFormEntity> entities = this.mob.level().getEntitiesOfClass(AstralFormEntity.class, boundingBox);
        double closestDistance = Double.MAX_VALUE;
        AstralFormEntity closestEntity = null;

        for (AstralFormEntity entity : entities) {
            double distance = this.mob.distanceToSqr(entity);
            if (distance < closestDistance) {
                closestDistance = distance;
                closestEntity = entity;
            }
        }

        this.targetEntity = closestEntity;
        return this.targetEntity != null;
    }

    @Override
    public void start() {
        this.mob.setTarget(this.targetEntity);
        super.start();
    }

    @Override
    public void stop() {
        this.mob.setTarget(null);
        super.stop();
    }

}
