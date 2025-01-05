package net.quackiemackie.pathoscraft.entity.client.spiderMount;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.entity.entity.SpiderMountEntity;

public class SpiderMountModel<T extends SpiderMountEntity> extends HierarchicalModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "spider_mount"), "main");
    private final ModelPart root;
    private final ModelPart head;

    public SpiderMountModel(ModelPart root) {
        this.root = root.getChild("root");
        this.head = this.root.getChild("head");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition root = partdefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 7.5F, 0.0F));

        PartDefinition chest = root.addOrReplaceChild("chest", CubeListBuilder.create().texOffs(0, 20).addBox(-5.0F, 0.0F, -6.0F, 10.0F, 6.0F, 12.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

        PartDefinition head = root.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 71).addBox(-3.0F, -1.5F, -4.5F, 6.0F, 5.0F, 5.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.5F, -6.0F));

        PartDefinition right_fang = head.addOrReplaceChild("right_fang", CubeListBuilder.create(), PartPose.offset(-2.0F, 0.0F, -4.0F));

        PartDefinition cube_r1 = right_fang.addOrReplaceChild("cube_r1", CubeListBuilder.create().texOffs(46, 17).addBox(0.5F, 2.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(78, 31).addBox(0.0F, 2.0F, -0.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 0.0F, -2.0F, 0.2618F, 0.2618F, 0.0F));

        PartDefinition left_fang = head.addOrReplaceChild("left_fang", CubeListBuilder.create(), PartPose.offset(2.0F, 0.0F, -4.0F));

        PartDefinition cube_r2 = left_fang.addOrReplaceChild("cube_r2", CubeListBuilder.create().texOffs(52, 17).addBox(-1.5F, 2.5F, -2.0F, 1.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
                .texOffs(80, 9).addBox(-2.0F, 2.0F, -0.5F, 2.0F, 2.0F, 3.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 0.0F, -2.0F, 0.2618F, -0.2618F, 0.0F));

        PartDefinition abdomen = root.addOrReplaceChild("abdomen", CubeListBuilder.create().texOffs(0, 0).addBox(-6.0F, -2.0F, -0.5F, 12.0F, 9.0F, 11.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, -0.5F, 6.5F));

        PartDefinition right_legs = root.addOrReplaceChild("right_legs", CubeListBuilder.create(), PartPose.offset(-5.0F, 0.5F, 0.0F));

        PartDefinition right_back_group = right_legs.addOrReplaceChild("right_back_group", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 1.0F));

        PartDefinition right_back = right_back_group.addOrReplaceChild("right_back", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 4.0F));

        PartDefinition cube_r3 = right_back.addOrReplaceChild("cube_r3", CubeListBuilder.create().texOffs(0, 38).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 0.0F, 0.0F, -1.1345F, 0.4363F));

        PartDefinition cube_r4 = right_back.addOrReplaceChild("cube_r4", CubeListBuilder.create().texOffs(22, 71).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 5.0F, 5.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r5 = right_back.addOrReplaceChild("cube_r5", CubeListBuilder.create().texOffs(78, 71).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -2.0F, 5.0F, 0.0F, 0.4363F, 0.4363F));

        PartDefinition right_front_middle = right_back_group.addOrReplaceChild("right_front_middle", CubeListBuilder.create(), PartPose.offset(-5.0F, -1.0F, -3.0F));

        PartDefinition cube_r6 = right_front_middle.addOrReplaceChild("cube_r6", CubeListBuilder.create().texOffs(30, 71).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 6.0F, -1.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition cube_r7 = right_front_middle.addOrReplaceChild("cube_r7", CubeListBuilder.create().texOffs(80, 0).addBox(-1.5F, 1.0F, -2.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -2.0F, 0.0F, 0.0F, -0.0873F, 0.4363F));

        PartDefinition cube_r8 = right_front_middle.addOrReplaceChild("cube_r8", CubeListBuilder.create().texOffs(0, 55).addBox(-1.0F, -1.0F, -11.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 0.0F, 0.0F, 1.4835F, 0.4363F));

        PartDefinition right_front_group = right_legs.addOrReplaceChild("right_front_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -1.0F));

        PartDefinition right_back_middle = right_front_group.addOrReplaceChild("right_back_middle", CubeListBuilder.create(), PartPose.offset(-1.0F, 0.0F, 3.0F));

        PartDefinition cube_r9 = right_back_middle.addOrReplaceChild("cube_r9", CubeListBuilder.create().texOffs(32, 55).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-1.0F, 2.0F, 0.0F, 0.0F, -1.4835F, 0.4363F));

        PartDefinition cube_r10 = right_back_middle.addOrReplaceChild("cube_r10", CubeListBuilder.create().texOffs(38, 71).addBox(-1.5F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-14.0F, 5.0F, 1.0F, 0.0F, 0.0873F, 0.0F));

        PartDefinition cube_r11 = right_back_middle.addOrReplaceChild("cube_r11", CubeListBuilder.create().texOffs(78, 80).addBox(-1.5F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-11.0F, -2.0F, 1.0F, 0.0F, 0.0873F, 0.4363F));

        PartDefinition right_front = right_front_group.addOrReplaceChild("right_front", CubeListBuilder.create(), PartPose.offset(-6.0F, -1.0F, -5.0F));

        PartDefinition cube_r12 = right_front.addOrReplaceChild("cube_r12", CubeListBuilder.create().texOffs(46, 71).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-9.0F, 6.0F, -4.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition cube_r13 = right_front.addOrReplaceChild("cube_r13", CubeListBuilder.create().texOffs(0, 81).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-6.0F, -1.0F, -4.0F, 0.0F, -0.4363F, 0.4363F));

        PartDefinition cube_r14 = right_front.addOrReplaceChild("cube_r14", CubeListBuilder.create().texOffs(34, 38).addBox(-1.0F, -1.0F, -11.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(4.0F, 3.0F, 1.0F, 0.0F, 1.1345F, 0.4363F));

        PartDefinition left_legs = root.addOrReplaceChild("left_legs", CubeListBuilder.create(), PartPose.offset(5.0F, 0.5F, 0.0F));

        PartDefinition left_front_group = left_legs.addOrReplaceChild("left_front_group", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, -1.0F));

        PartDefinition left_front = left_front_group.addOrReplaceChild("left_front", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, -4.0F));

        PartDefinition cube_r15 = left_front.addOrReplaceChild("cube_r15", CubeListBuilder.create().texOffs(44, 20).addBox(-1.0F, -1.0F, -11.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.0F, 0.0F, 0.0F, -1.1345F, -0.4363F));

        PartDefinition cube_r16 = left_front.addOrReplaceChild("cube_r16", CubeListBuilder.create().texOffs(54, 71).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 5.0F, -5.0F, 0.0F, 0.4363F, 0.0F));

        PartDefinition cube_r17 = left_front.addOrReplaceChild("cube_r17", CubeListBuilder.create().texOffs(8, 81).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, -2.0F, -5.0F, 0.0F, 0.4363F, -0.4363F));

        PartDefinition left_back_middle = left_front_group.addOrReplaceChild("left_back_middle", CubeListBuilder.create(), PartPose.offset(5.0F, -1.0F, 3.0F));

        PartDefinition cube_r18 = left_back_middle.addOrReplaceChild("cube_r18", CubeListBuilder.create().texOffs(64, 55).addBox(-1.0F, -1.0F, -3.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 0.0F, 0.0F, 1.4835F, -0.4363F));

        PartDefinition cube_r19 = left_back_middle.addOrReplaceChild("cube_r19", CubeListBuilder.create().texOffs(62, 71).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 6.0F, 1.0F, 0.0F, -0.0873F, 0.0F));

        PartDefinition cube_r20 = left_back_middle.addOrReplaceChild("cube_r20", CubeListBuilder.create().texOffs(16, 85).addBox(-0.5F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -1.0F, 1.0F, 0.0F, -0.0873F, -0.4363F));

        PartDefinition left_back_group = left_legs.addOrReplaceChild("left_back_group", CubeListBuilder.create(), PartPose.offset(0.0F, 0.0F, 1.0F));

        PartDefinition left_back = left_back_group.addOrReplaceChild("left_back", CubeListBuilder.create(), PartPose.offset(1.0F, 0.0F, 4.0F));

        PartDefinition cube_r21 = left_back.addOrReplaceChild("cube_r21", CubeListBuilder.create().texOffs(46, 0).addBox(-1.0F, -1.0F, -4.0F, 2.0F, 2.0F, 15.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(1.0F, 2.0F, 0.0F, 0.0F, 1.1345F, -0.4363F));

        PartDefinition cube_r22 = left_back.addOrReplaceChild("cube_r22", CubeListBuilder.create().texOffs(70, 71).addBox(-1.0F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(14.0F, 5.0F, 5.0F, 0.0F, -0.4363F, 0.0F));

        PartDefinition cube_r23 = left_back.addOrReplaceChild("cube_r23", CubeListBuilder.create().texOffs(24, 85).addBox(-1.0F, 0.0F, -1.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(11.0F, -2.0F, 5.0F, 0.0F, -0.4363F, -0.4363F));

        PartDefinition left_front_middle = left_back_group.addOrReplaceChild("left_front_middle", CubeListBuilder.create(), PartPose.offset(6.0F, -1.0F, -3.0F));

        PartDefinition cube_r24 = left_front_middle.addOrReplaceChild("cube_r24", CubeListBuilder.create().texOffs(68, 37).addBox(-1.0F, -1.0F, -11.0F, 2.0F, 2.0F, 14.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-4.0F, 3.0F, 0.0F, 0.0F, -1.4835F, -0.4363F));

        PartDefinition cube_r25 = left_front_middle.addOrReplaceChild("cube_r25", CubeListBuilder.create().texOffs(32, 85).addBox(-0.5F, 1.0F, -2.0F, 2.0F, 7.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(6.0F, -2.0F, 0.0F, 0.0F, 0.0873F, -0.4363F));

        PartDefinition cube_r26 = left_front_middle.addOrReplaceChild("cube_r26", CubeListBuilder.create().texOffs(78, 17).addBox(-0.5F, -0.5F, -1.0F, 2.0F, 12.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(9.0F, 6.0F, -1.0F, 0.0F, 0.0873F, 0.0F));

        return LayerDefinition.create(meshdefinition, 128, 128);
    }

    @Override
    public void setupAnim(SpiderMountEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.applyHeadRotation(netHeadYaw, headPitch);

        this.animateWalk(SpiderMountAnimations.SPIDER_MOUNT_WALK, limbSwing, limbSwingAmount, 2f, 2.5f);
        this.animate(entity.eatAnimationState, SpiderMountAnimations.SPIDER_MOUNT_EAT, ageInTicks, 1f);
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
