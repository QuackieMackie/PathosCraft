package io.github.quackiemackie.pathoscraft.item.renderer;

import io.github.quackiemackie.pathoscraft.item.items.misc.AstralLantern;
import io.github.quackiemackie.pathoscraft.item.model.AstralLanternModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AstralLanternRenderer extends GeoItemRenderer<AstralLantern> {

    public AstralLanternRenderer() {
        super(new AstralLanternModel());
    }
}

