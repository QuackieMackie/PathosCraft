package net.quackiemackie.pathoscraft.handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class QuestHandler {
    private static final List<Quest> quests = new ArrayList<>();

    /**
     * Loads quests from the JSON files in the specified directory.
     *
     * @param resourceManager the resource manager to get the quest files.
     */
    public void loadQuests(ResourceManager resourceManager) {
        ResourceLocation questFolder = ResourceLocation.fromNamespaceAndPath(PathosCraft.MOD_ID, "quest");

        Map<ResourceLocation, Resource> resources = resourceManager.listResources(questFolder.getPath(), path -> path.getPath().endsWith(".json"));

        quests.clear();

        for (Map.Entry<ResourceLocation, Resource> entry : resources.entrySet()) {
            ResourceLocation resourceLocation = entry.getKey();
            Resource resource = entry.getValue();
            try (InputStreamReader reader = new InputStreamReader(resource.open())) {
                JsonElement jsonElement = JsonParser.parseReader(reader);
                Quest quest = Quest.CODEC.parse(JsonOps.INSTANCE, jsonElement)
                        .result()
                        .orElseThrow(() -> new IllegalStateException("Failed to parse quest: " + jsonElement));
                quests.add(quest);
            } catch (IOException e) {
                Logger.getLogger(PathosCraft.MOD_ID).severe("Failed to load quest from file: " + resourceLocation + e.getMessage());
            }
        }
        Logger.getLogger(PathosCraft.MOD_ID).info("Loaded " + quests.size() + " quests.");
    }

    /**
     * Gets the list of all loaded quests.
     *
     * @return the list of loaded quests.
     */
    public static List<Quest> getQuests() {
        return quests;
    }

    /**
     * Finds a quest by its ID.
     *
     * @param questId the ID of the quest to find.
     * @return the quest with the specified ID, or null if not found.
     */
    public static Quest getQuestById(int questId) {
        return quests.stream()
                .filter(quest -> quest.getId() == questId)
                .findFirst()
                .orElse(null);
    }
}