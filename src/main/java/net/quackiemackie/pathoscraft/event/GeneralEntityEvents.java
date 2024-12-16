package net.quackiemackie.pathoscraft.event;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.entity.AstralFormEntity;
import net.quackiemackie.pathoscraft.entity.ai.goal.TargetAstralFormGoal;
import net.quackiemackie.pathoscraft.util.handlers.abilities.AstralFormHandler;

@EventBusSubscriber(modid = PathosCraft.MOD_ID)
public class GeneralEntityEvents {

    @SubscribeEvent
    public static void onEntityJoinLevel(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof Monster monster) {
            monster.targetSelector.addGoal(3, new TargetAstralFormGoal(monster));
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof AstralFormEntity astralFormEntity) {
            AstralFormHandler.handleAstralFormEntityDeath(astralFormEntity);
        }
    }
}
