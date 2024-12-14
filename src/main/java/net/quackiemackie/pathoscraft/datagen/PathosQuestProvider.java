package net.quackiemackie.pathoscraft.datagen;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.quest.Quest;
import net.quackiemackie.pathoscraft.quest.QuestObjective;
import net.quackiemackie.pathoscraft.quest.QuestReward;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PathosQuestProvider implements DataProvider {

    private static final int DEFAULT_PROGRESS = 0;
    private static final int ACTIVE_QUEST_SLOT = 0;

    private final PackOutput output;
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
            .serializeNulls()
            .create();

    public PathosQuestProvider(PackOutput output) {
        this.output = output;
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cache) {
        return generateQuests(cache, output.getOutputFolder());
    }

    @Override
    public String getName() {
        return "PathosCraft Quests";
    }

    private CompletableFuture<?> generateQuests(CachedOutput cache, Path outputFolder) {
        List<Quest> quests = new ArrayList<>();

        quests.add(new Quest(1, "Main quest 1", "Description for test quest.", "minecraft:dirt", 0, 0, 0, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("collect", "minecraft:dirt", 10, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

        quests.add(new Quest(2, "Main quest 2", "Description for test quest 2.", "minecraft:stone", 0, 1, 1, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("kill", "minecraft:zombie", 10, DEFAULT_PROGRESS),
                        new QuestObjective("collect", "pathoscraft:raw_sadness", 5, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:gold_ingot", 1))
        ));

        quests.add(new Quest(3, "Main quest 3", "Description for test quest 3.", "minecraft:diamond_block", 0, 2, 2, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("kill", "minecraft:skeleton", 10, DEFAULT_PROGRESS),
                        new QuestObjective("collect", "pathoscraft:raw_sadness", 5, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:gold_ingot", 1))
        ));

        quests.add(new Quest(4, "Main quest 4", "Description for test quest 4.", "minecraft:apple", 0, 3, 3, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("kill", "minecraft:skeleton", 10, DEFAULT_PROGRESS),
                        new QuestObjective("collect", "minecraft:apple", 5, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:gold_ingot", 1))
        ));

        quests.add(new Quest(5, "Side Quest 1", "Description for test quest 5.", "minecraft:gold_block", 1, 0, 0, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("kill", "minecraft:ghast", 10, DEFAULT_PROGRESS),
                        new QuestObjective("collect", "minecraft:dirt", 5, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

        quests.add(new Quest(6, "Optional Quest 1", "Description for test quest 6.", "pathoscraft:raw_sadness", 2, 0, 0, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("kill", "minecraft:chicken", 10, DEFAULT_PROGRESS),
                        new QuestObjective("kill", "minecraft:pig", 5, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

        quests.add(new Quest(7, "Optional Quest 2", "Description for test quest 7.", "minecraft:gold_ingot", 2, 0, 1, ACTIVE_QUEST_SLOT,
                List.of(new QuestObjective("collect", "minecraft:dirt", 5, DEFAULT_PROGRESS)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

        for (int i = 0; i < 1000; i++) {
            int questId = 101 + i;
            String questName = "Optional Quest " + (i + 1);
            String description = "Description for optional quest " + (i + 1) + ".";
            String iconItem = (i % 2 == 0) ? "pathoscraft:raw_sadness" : "minecraft:emerald";
            int slot = 2 + i;

            List<QuestObjective> objectives = new ArrayList<>();
            objectives.add(new QuestObjective("collect", "minecraft:dirt", (i + 1), DEFAULT_PROGRESS));

            List<QuestReward> rewards = new ArrayList<>();
            rewards.add(new QuestReward("give", (i % 2 == 0) ? "minecraft:diamond" : "minecraft:gold_ingot", (i % 5) + 1));

            quests.add(new Quest(questId, questName, description, iconItem, 2, 0, slot, ACTIVE_QUEST_SLOT, objectives, rewards));
        }

        List<CompletableFuture<?>> futures = new ArrayList<>();
        for (Quest quest : quests) {
            Path questPath = generateQuestPath(outputFolder, quest);
            JsonElement jsonElement = GSON.toJsonTree(quest);
            futures.add(DataProvider.saveStable(cache, jsonElement, questPath));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private Path generateQuestPath(Path outputFolder, Quest quest) {
        String questFileName = String.format("quest_%d.json", quest.getQuestId());
        return outputFolder.resolve(String.format("data/%s/quest/%s", PathosCraft.MOD_ID, questFileName));
    }
}