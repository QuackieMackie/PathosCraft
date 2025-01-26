package io.github.quackiemackie.pathoscraft.util.handlers.workers;

import io.github.quackiemackie.pathoscraft.util.worker.FilledMap;
import io.github.quackiemackie.pathoscraft.util.worker.WorkerStationMaps;

import java.util.ArrayList;
import java.util.List;

public class WorkerPayloadHandler {
    private static WorkerStationMaps workerMapData = new WorkerStationMaps(new ArrayList<>());

    public static void setWorkerMapData(WorkerStationMaps mapData) {
        workerMapData = mapData;
    }

    public static WorkerStationMaps getWorkerMapData() {
        return workerMapData;
    }

    public static void updateSingleWorkerMap(FilledMap filledMap) {
        List<FilledMap> originalMaps = workerMapData.maps();

        List<FilledMap> mutableMaps = new ArrayList<>(originalMaps);

        for (int i = 0; i < mutableMaps.size(); i++) {
            if (mutableMaps.get(i).slot() == filledMap.slot()) {
                mutableMaps.set(i, filledMap);
                workerMapData = new WorkerStationMaps(mutableMaps);
                return;
            }
        }

        mutableMaps.add(filledMap);
        workerMapData = new WorkerStationMaps(mutableMaps);
    }
}