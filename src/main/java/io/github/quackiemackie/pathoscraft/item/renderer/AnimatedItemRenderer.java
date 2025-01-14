package io.github.quackiemackie.pathoscraft.item.renderer;

import io.github.quackiemackie.pathoscraft.item.advanced.AnimatedItem;
import io.github.quackiemackie.pathoscraft.item.model.AnimatedItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<AnimatedItem> {

    public AnimatedItemRenderer() {
        super(new AnimatedItemModel());
    }

}
