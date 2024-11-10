package net.quackiemackie.pathoscraft.event;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.ModEntities;
import net.quackiemackie.pathoscraft.entity.advanced.AstralFormEntity;
import net.quackiemackie.pathoscraft.entity.advanced.SpiderMountEntity;

import static net.quackiemackie.pathoscraft.entity.ModEntities.ASTRAL_FORM;

@EventBusSubscriber(modid = PathosCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EntityEvents {

    @SubscribeEvent
    public static void initializeAttributes(EntityAttributeCreationEvent event) {
        event.put(ASTRAL_FORM.get(), AstralFormEntity.createAttributes().build());

        event.put(ModEntities.SPIDER_MOUNT.get(), SpiderMountEntity.createSpiderAttributes().build());
    }
}

