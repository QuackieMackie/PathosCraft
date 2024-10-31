package net.quackiemackie.pathoscraft.item.client;

import net.quackiemackie.pathoscraft.item.advanced.AnimatedItem;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class AnimatedItemRenderer extends GeoItemRenderer<AnimatedItem> {

    public AnimatedItemRenderer() {
        super(new AnimatedItemModel());
    }

}
