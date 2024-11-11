package net.quackiemackie.pathoscraft.item.renderer;

import net.quackiemackie.pathoscraft.item.advanced.AnimatedItem;
import net.quackiemackie.pathoscraft.item.model.AnimatedItemModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<AnimatedItem> {

    public AnimatedItemRenderer() {
        super(new AnimatedItemModel());
    }

}
