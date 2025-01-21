package io.github.quackiemackie.pathoscraft.item.items.workers;

import io.github.quackiemackie.pathoscraft.registers.PathosDataComponents;
import io.github.quackiemackie.pathoscraft.util.worker.Worker;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

public class NaiveWorker extends Item {
    private static final int PROGRESS_TIME = 100;
    private int tickCount = 0;

    public NaiveWorker(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (tooltipFlag.isCreative()) {
            return;
        }

        if (stack.get(PathosDataComponents.WORKER_ITEM_DATA.get()) == null) {
            tooltipComponents.add(Component.literal("Hold SHIFT to uncover the Naive Worker..."));

            if (tooltipFlag.hasShiftDown()) {
                trackProgress(stack, tooltipComponents);
            } else if (tickCount > 0) {
                resetTickCount();
            }
        } else {
            Worker worker = stack.get(PathosDataComponents.WORKER_ITEM_DATA.get());
            tooltipComponents.add(Component.literal("Naive Worker Data:"));
            tooltipComponents.add(Component.literal(" - ID: " + worker.id()));
            tooltipComponents.add(Component.literal(" - Efficiency: " + worker.efficiency()));
            tooltipComponents.add(Component.literal(" - Speed: " + worker.speed()));
            tooltipComponents.add(Component.literal(" - Luck: " + worker.luck()));
        }
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private void trackProgress(ItemStack stack, List<Component> tooltipComponents) {
        tickCount++;

        int progress = Math.min(tickCount, PROGRESS_TIME);

        int maxWidth = tooltipComponents.stream()
                .mapToInt(component -> Minecraft.getInstance().font.width(component.getString()))
                .max()
                .orElse(0);

        int characterWidth = Math.max(1, Minecraft.getInstance().font.width("X"));
        int charactersAvailable = maxWidth / characterWidth;

        StringBuilder progressBar = new StringBuilder();
        int filledBars = (int) ((progress / (double) PROGRESS_TIME) * charactersAvailable);

        for (int i = 0; i < charactersAvailable; i++) {
            if (i < filledBars) {
                progressBar.append("=");
            } else {
                progressBar.append(" ");
            }
        }

        tooltipComponents.add(Component.literal(progressBar.toString()));

        if (progress >= PROGRESS_TIME) {
            generateWorkerData(stack);
            tooltipComponents.add(Component.literal("Naive Worker Uncovered!"));
            tickCount = 0;
        }
    }

    private void resetTickCount() {
        this.tickCount = 0;
    }

    private void generateWorkerData(ItemStack stack) {
        if (stack.get(PathosDataComponents.WORKER_ITEM_DATA.get()) == null) {
            stack.set(PathosDataComponents.WORKER_ITEM_DATA.get(), Worker.naiveWorker(stack.hashCode(), List.of()));
        }
    }
}