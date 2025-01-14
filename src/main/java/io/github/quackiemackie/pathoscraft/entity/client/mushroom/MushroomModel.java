package io.github.quackiemackie.pathoscraft.entity.client.mushroom;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.entity.entity.MushroomEntity;

public class MushroomModel<T extends MushroomEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "mushroom"), "main");
    private final ModelPart root;
    private final ModelPart full_body;
    private final ModelPart head;
    private final ModelPart backpack;

    public MushroomModel(ModelPart root) {
        this.root = root.getChild("root");
        this.full_body = this.root.getChild("full_body");
        this.head = this.full_body.getChild("head");
        this.backpack = this.full_body.getChild("backpack");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));

        PartDefinition full_body = root.addOrReplaceChild("full_body", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = full_body.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-7.0F, -9.5F, -7.0F, 14.0F, 11.0F, 14.0F, new CubeDeformation(0.0F))
                .texOffs(49, 26).addBox(-5.0F, -11.0F, -5.0F, 10.0F, 2.0F, 10.0F, new CubeDeformation(0.0F))
                .texOffs(0, 26).addBox(-6.0F, -8.5F, -6.0F, 12.0F, 10.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -12.0F, 0.0F));

        PartDefinition body = full_body.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 49).mirror().addBox(-4.5F, -7.0F, -4.5F, 9.0F, 12.0F, 9.0F, new CubeDeformation(0.0F)).mirror(false), PartPose.offset(0.0F, -7.0F, 0.0F));

        PartDefinition backpack = full_body.addOrReplaceChild("backpack", CubeListBuilder.create().texOffs(37, 58).addBox(-4.0F, -3.5F, -0.5F, 8.0F, 8.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -7.0F, 5.0F));

        PartDefinition right_arm = root.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(33, 49).addBox(-2.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-4.5F, -9.5F, 0.0F));

        PartDefinition left_arm = root.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(49, 39).addBox(0.0F, 0.0F, -1.0F, 2.0F, 6.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(4.5F, -9.5F, 0.0F));

        PartDefinition right_leg = root.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(42, 49).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(-2.0F, -2.0F, 0.0F));

        PartDefinition left_leg = root.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(51, 48).addBox(-1.0F, -1.0F, -1.0F, 2.0F, 3.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offset(2.0F, -2.0F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(MushroomEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(MushroomAnimations.MUSHROOM_WALKING, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.sitAnimationState, MushroomAnimations.MUSHROOM_SIT, ageInTicks, 1f);
        this.animate(entity.standAnimationState, MushroomAnimations.MUSHROOM_STAND, ageInTicks, 1f);

        if (entity.isSitting) this.backpack.visible = true;
    }

    private void applyHeadRotation(float headYaw, float headPitch) {
        headYaw = Mth.clamp(headYaw, -30f, 30f);
        headPitch = Mth.clamp(headPitch, -25f, 45);

        this.head.yRot = headYaw * ((float)Math.PI / 180f);
        this.head.xRot = headPitch *  ((float)Math.PI / 180f);
    }

    @Override
    public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, int color) {
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay, color);
    }

    @Override
    public ModelPart root() {
        return root;
    }
}
