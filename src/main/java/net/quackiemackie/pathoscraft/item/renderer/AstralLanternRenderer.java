package net.quackiemackie.pathoscraft.item.renderer;

import net.quackiemackie.pathoscraft.item.advanced.AstralLantern;
import net.quackiemackie.pathoscraft.item.model.AstralLanternModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AstralLanternRenderer extends GeoItemRenderer<AstralLantern> {

    public AstralLanternRenderer() {
        super(new AstralLanternModel());
    }
}

