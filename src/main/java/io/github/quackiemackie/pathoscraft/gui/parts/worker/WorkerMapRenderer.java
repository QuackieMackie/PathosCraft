package io.github.quackiemackie.pathoscraft.gui.parts.worker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.gui.map.MapDecorationRendererManager;
import org.joml.Matrix4f;

import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class WorkerMapRenderer implements AutoCloseable {
    final TextureManager textureManager;
    final MapDecorationTextureManager decorationTextures;
    private final Int2ObjectMap<WorkerMapRenderer.MapInstance> maps = new Int2ObjectOpenHashMap<>();

    public WorkerMapRenderer(TextureManager textureManager, MapDecorationTextureManager decorationTextures) {
        this.textureManager = textureManager;
        this.decorationTextures = decorationTextures;
    }

    public void render(PoseStack poseStack, MultiBufferSource buffer, MapId mapId, MapItemSavedData mapData, boolean active, int packedLight) {
        this.getOrCreateMapInstance(mapId, mapData).draw(poseStack, buffer, active, packedLight);
    }

    private MapInstance getOrCreateMapInstance(MapId mapId, MapItemSavedData mapData) {
        return this.maps.compute(mapId.id(), (integer, mapInstance) -> {
            if (mapInstance == null) {
                return new MapInstance(integer, mapData);
            } else {
                mapInstance.replaceMapData(mapData);
                return mapInstance;
            }
        });
    }

    public void resetData() {
        for (MapInstance maprenderer$mapinstance : this.maps.values()) {
            maprenderer$mapinstance.close();
        }

        this.maps.clear();
    }

    public void close() {
        this.resetData();
    }

    @OnlyIn(Dist.CLIENT)
    class MapInstance implements AutoCloseable {
        private MapItemSavedData data;
        private final DynamicTexture texture;
        private final RenderType renderType;
        private boolean requiresUpload = true;

        MapInstance(int id, MapItemSavedData data) {
            this.data = data;
            this.texture = new DynamicTexture(128, 128, true);
            ResourceLocation resourcelocation = WorkerMapRenderer.this.textureManager.register("map/" + id, this.texture);
            this.renderType = RenderType.text(resourcelocation);
        }

        void replaceMapData(MapItemSavedData data) {
            boolean flag = this.data != data;
            this.data = data;
            this.requiresUpload |= flag;
        }

        private void updateTexture() {
            for(int i = 0; i < 128; ++i) {
                for(int j = 0; j < 128; ++j) {
                    int k = j + i * 128;
                    Objects.requireNonNull(this.texture.getPixels()).setPixelRGBA(j, i, MapColor.getColorFromPackedId(this.data.colors[k]));
                }
            }

            this.texture.upload();
        }

        void draw(PoseStack poseStack, MultiBufferSource bufferSource, boolean active, int packedLight) {
            if (this.requiresUpload) {
                this.updateTexture();
                this.requiresUpload = false;
            }

            Matrix4f matrix4f = poseStack.last().pose();
            VertexConsumer vertexconsumer = bufferSource.getBuffer(this.renderType);
            vertexconsumer.addVertex(matrix4f, 0.0F, 128.0F, -0.01F).setColor(-1).setUv(0.0F, 1.0F).setLight(packedLight);
            vertexconsumer.addVertex(matrix4f, 128.0F, 128.0F, -0.01F).setColor(-1).setUv(1.0F, 1.0F).setLight(packedLight);
            vertexconsumer.addVertex(matrix4f, 128.0F, 0.0F, -0.01F).setColor(-1).setUv(1.0F, 0.0F).setLight(packedLight);
            vertexconsumer.addVertex(matrix4f, 0.0F, 0.0F, -0.01F).setColor(-1).setUv(0.0F, 0.0F).setLight(packedLight);
            int k = 0;

            for(MapDecoration mapdecoration : this.data.getDecorations()) {
                ResourceLocation assetId = mapdecoration.type().value().assetId();

                //Blocks the player being seen on the map.
                if (assetId.toString().equals("minecraft:player_off_map") || assetId.toString().equals("minecraft:player")) {
                    continue;
                }

                if (!active || mapdecoration.renderOnFrame()) {
                    if (!MapDecorationRendererManager.render(mapdecoration, poseStack, bufferSource, this.data, WorkerMapRenderer.this.decorationTextures, active, packedLight, k)) {
                        poseStack.pushPose();
                        poseStack.translate(0.0F + (float) mapdecoration.x() / 2.0F + 64.0F, 0.0F + (float) mapdecoration.y() / 2.0F + 64.0F, -0.02F);
                        poseStack.mulPose(Axis.ZP.rotationDegrees((float) (mapdecoration.rot() * 360) / 16.0F));
                        poseStack.scale(4.0F, 4.0F, 3.0F);
                        poseStack.translate(-0.125F, 0.125F, 0.0F);
                        Matrix4f matrix4f1 = poseStack.last().pose();
                        TextureAtlasSprite textureatlassprite = WorkerMapRenderer.this.decorationTextures.get(mapdecoration);
                        float f2 = textureatlassprite.getU0();
                        float f3 = textureatlassprite.getV0();
                        float f4 = textureatlassprite.getU1();
                        float f5 = textureatlassprite.getV1();
                        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(textureatlassprite.atlasLocation()));
                        vertexConsumer.addVertex(matrix4f1, -1.0F, 1.0F, (float) k * -0.001F).setColor(-1).setUv(f2, f3).setLight(packedLight);
                        vertexConsumer.addVertex(matrix4f1, 1.0F, 1.0F, (float) k * -0.001F).setColor(-1).setUv(f4, f3).setLight(packedLight);
                        vertexConsumer.addVertex(matrix4f1, 1.0F, -1.0F, (float) k * -0.001F).setColor(-1).setUv(f4, f5).setLight(packedLight);
                        vertexConsumer.addVertex(matrix4f1, -1.0F, -1.0F, (float) k * -0.001F).setColor(-1).setUv(f2, f5).setLight(packedLight);
                        poseStack.popPose();
                    }
                    ++k;
                }
            }

        }

        public void close() {
            this.texture.close();
        }
    }
}
