package io.github.quackiemackie.pathoscraft.util.worker;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record WorkerNodeList(List<WorkerNode> nodes) {
    public static final Codec<WorkerNodeList> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.list(WorkerNode.CODEC).fieldOf("nodes").forGetter(WorkerNodeList::nodes)
    ).apply(instance, WorkerNodeList::new));

    @Override
    public String toString() {
        return "WorkerNodeList: " + nodes.toString();
    }

    public record WorkerNode(int x, int y) {
        public static final Codec<WorkerNode> CODEC = RecordCodecBuilder.create(instance -> instance.group(
                Codec.INT.fieldOf("x").forGetter(WorkerNode::x),
                Codec.INT.fieldOf("y").forGetter(WorkerNode::y)
        ).apply(instance, WorkerNode::new));

        @Override
        public String toString() {
            return "Node: {" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }
}