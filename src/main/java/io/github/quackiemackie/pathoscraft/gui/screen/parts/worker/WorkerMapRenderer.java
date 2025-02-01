package io.github.quackiemackie.pathoscraft.gui.screen.parts.worker;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBE;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
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
        }

        public void close() {
            this.texture.close();
        }
    }

    public static void renderMapOnBlockEntity(WorkerMapRenderer workerMapRenderer, PoseStack poseStack, MultiBufferSource bufferSource,
                                              WorkerStationBE blockEntity, float scaleFactor, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();

        // Extract map IDs into a grid
        Integer[][] mapIds = extractMapIds(blockEntity);

        float mapSize = 128 * scaleFactor;

        // Iterate over the grid and render maps
        for (int row = 0; row < mapIds.length; row++) {
            for (int col = 0; col < mapIds[row].length; col++) {
                int mapId = mapIds[row][col];

                if (mapId == -1) continue;

                // Fetch map data
                MapItemSavedData mapData = minecraft.level.getMapData(new MapId(mapId));
                if (mapData == null) continue;

                // Push pose, translate and render
                poseStack.pushPose();
                positionForMap(poseStack, row, col, mapSize);
                workerMapRenderer.render(poseStack, bufferSource, new MapId(mapId), mapData, false, packedLight);
                poseStack.popPose();
            }
        }
    }


    private static Integer[][] extractMapIds(WorkerStationBE blockEntity) {
        Integer[][] mapIds = new Integer[3][3];

        for (int i = 0; i < 9; i++) {
            ItemStack stack = blockEntity.inventory.getStackInSlot(i);
            MapId mapIdComponent = (stack.isEmpty() || stack.get(DataComponents.MAP_ID) == null) ? null : stack.get(DataComponents.MAP_ID);
            mapIds[i / 3][i % 3] = (mapIdComponent != null) ? mapIdComponent.id() : -1;
        }

        return mapIds;
    }

    private static void positionForMap(PoseStack poseStack, int row, int col, float mapSize) {
        float offsetX = (col - 1) * mapSize;
        float offsetZ = (row - 1) * mapSize;

        poseStack.translate(0.34 + offsetX, 1.01, 0.34 + offsetZ);
        poseStack.mulPose(Axis.XP.rotationDegrees(270));
        poseStack.scale(0.0025f, -0.0025f, 0.0025f);
    }
}