package net.quackiemackie.pathoscraft.event.server;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.client.astralForm.AstralFormModel;
import net.quackiemackie.pathoscraft.entity.client.spiderMount.SpiderMountModel;
import net.quackiemackie.pathoscraft.entity.entity.AstralFormEntity;
import net.quackiemackie.pathoscraft.entity.entity.SpiderMountEntity;
import net.quackiemackie.pathoscraft.entity.PathosEntities;

@EventBusSubscriber(modid = PathosCraft.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class EntityEvents {

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(PathosEntities.ASTRAL_FORM.get(), AstralFormEntity.createAttributes().build());
        event.put(PathosEntities.SPIDER_MOUNT.get(), SpiderMountEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(AstralFormModel.LAYER_LOCATION, AstralFormModel::createBodyLayer);
        event.registerLayerDefinition(SpiderMountModel.LAYER_LOCATION, SpiderMountModel::createBodyLayer);
    }
}

