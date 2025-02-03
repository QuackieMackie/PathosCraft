package io.github.quackiemackie.pathoscraft.gui.screen.parts.worker;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.github.quackiemackie.pathoscraft.PathosCraft;
import io.github.quackiemackie.pathoscraft.block.entity.WorkerStationBE;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerNodeList;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.resources.MapDecorationTextureManager;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.saveddata.maps.MapId;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

import java.util.Map;
import java.util.Objects;

@OnlyIn(Dist.CLIENT)
public class WorkerMapRenderer implements AutoCloseable {
    final TextureManager textureManager;
    final MapDecorationTextureManager decorationTextures;
    private final Int2ObjectMap<WorkerMapRenderer.MapInstance> maps = new Int2ObjectOpenHashMap<>();

    private final static ResourceLocation WORKER_NODE_TEXTURE = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "textures/screen/worker_map_node.png");

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

    public static void renderMapOnBE(WorkerMapRenderer workerMapRenderer, PoseStack poseStack, MultiBufferSource bufferSource, WorkerStationBE blockEntity, float scaleFactor, int packedLight) {
        Minecraft minecraft = Minecraft.getInstance();
        Integer[][] mapIds = extractMapIds(blockEntity);

        float mapSize = 128 * scaleFactor;
        float gridHalfSize = 1.5f * mapSize;

        poseStack.pushPose();
        poseStack.translate(-gridHalfSize, 0, -gridHalfSize);

        for (int row = 0; row < mapIds.length; row++) {
            for (int col = 0; col < mapIds[row].length; col++) {
                int mapId = mapIds[row][col];

                if (mapId == -1) continue;

                MapItemSavedData mapData = minecraft.level.getMapData(new MapId(mapId));
                if (mapData == null) continue;

                poseStack.pushPose();
                positionForMap(poseStack, row, col, mapSize);
                workerMapRenderer.render(poseStack, bufferSource, new MapId(mapId), mapData, false, packedLight);
                poseStack.popPose();
            }
        }

        renderNodesOnBE(poseStack, bufferSource, blockEntity.getWorkerNodes(), scaleFactor, packedLight);
        poseStack.popPose();
    }

    public static void renderNodesOnBE(PoseStack poseStack, MultiBufferSource bufferSource, WorkerNodeList workerNodeList, float scaleFactor, int packedLight) {
        if (workerNodeList != null && !workerNodeList.nodes().isEmpty()) {
            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.text(WORKER_NODE_TEXTURE));

            for (WorkerNodeList.WorkerNode node : workerNodeList.nodes()) {
                float normalizedX = (node.x() / 512.0f) * 128.0f;
                float normalizedY = (node.y() / 512.0f) * 128.0f;

                float renderX = normalizedX * scaleFactor;
                float renderY = normalizedY * scaleFactor;

                float nodeScale = 0.005f * 128 * scaleFactor / 0.5f;

                poseStack.pushPose();

                poseStack.translate(0.5 + renderX * 2, 1.01f, 0.5 + renderY * 2);
                poseStack.scale(nodeScale, nodeScale, nodeScale);
                poseStack.mulPose(Axis.XP.rotationDegrees(90));

                Matrix4f matrix = poseStack.last().pose();
                int color = FastColor.ARGB32.color(255, 255, 255, 255);

                vertexConsumer.addVertex(matrix, -16, -16, 0F)
                        .setColor(color)
                        .setUv(0.0F, 0.0F)
                        .setUv2(packedLight & 0xFFFF, (packedLight >> 16) & 0xFFFF);

                vertexConsumer.addVertex(matrix, -16, 16, 0F)
                        .setColor(color)
                        .setUv(0.0F, 1.0F)
                        .setUv2(packedLight & 0xFFFF, (packedLight >> 16) & 0xFFFF);

                vertexConsumer.addVertex(matrix, 16, 16, 0F)
                        .setColor(color)
                        .setUv(1.0F, 1.0F)
                        .setUv2(packedLight & 0xFFFF, (packedLight >> 16) & 0xFFFF);

                vertexConsumer.addVertex(matrix, 16, -16, 0F)
                        .setColor(color)
                        .setUv(1.0F, 0.0F)
                        .setUv2(packedLight & 0xFFFF, (packedLight >> 16) & 0xFFFF);

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
        float offsetX = col * mapSize;
        float offsetZ = row * mapSize;

        poseStack.translate(offsetX + 0.5, 1.01, offsetZ + 0.5);
        poseStack.mulPose(Axis.XP.rotationDegrees(270));
        poseStack.scale(0.0025f, -0.0025f, 0.0025f);
    }

    /**
     * Renders a 3x3 grid of maps within the specified widget region.
     * Each map is scaled and positioned
     * inside the widget bounds using the provided dimensions and current currentScale level.
     *
     * @param guiGraphics The graphics context used for rendering the widget and maps.
     * @param widgetX The x-coordinate of the top-left corner of the widget.
     * @param widgetY The y-coordinate of the top-left corner of the widget.
     * @param widgetWidth The width of the widget.
     * @param widgetHeight The height of the widget.
     */
    public void renderWidgetMapGrid(GuiGraphics guiGraphics, int widgetX, int widgetY, int widgetWidth, int widgetHeight, Map<Integer, Integer> slotMapData, float currentScale) {
        Minecraft minecraft = Minecraft.getInstance();

        int[][] mapIds = {
                {slotMapData.getOrDefault(0, -1), slotMapData.getOrDefault(1, -1), slotMapData.getOrDefault(2, -1)},
                {slotMapData.getOrDefault(3, -1), slotMapData.getOrDefault(4, -1), slotMapData.getOrDefault(5, -1)},
                {slotMapData.getOrDefault(6, -1), slotMapData.getOrDefault(7, -1), slotMapData.getOrDefault(8, -1)}
        };

        int rows = 3;
        int cols = 3;
        double cellWidth = widgetWidth / (cols * currentScale);
        double cellHeight = widgetHeight / (rows * currentScale);

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                int mapId = mapIds[row][col];
                if (mapId == -1) {
                    continue;
                }

                double cellX = widgetX + col * cellWidth * currentScale;
                double cellY = widgetY + row * cellHeight * currentScale;

                MapItemSavedData mapState = minecraft.level.getMapData(new MapId(mapId));
                if (mapState == null) {
                    continue;
                }

                float scale = (float) (Math.min(cellWidth / 128, cellHeight / 128) * currentScale);

                RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(cellX, cellY, 25);
                guiGraphics.pose().scale(scale, scale, 1.0F);
                this.render(guiGraphics.pose(), guiGraphics.bufferSource(), new MapId(mapId), mapState, false, 0xf000f0);
                guiGraphics.pose().popPose();
            }
        }
    }
}