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

        quests.add(new Quest(1, "Main quest 1", "Description for test quest.", "minecraft:dirt", 0, 0, 0,
                List.of(new QuestObjective("collect", "minecraft:dirt", 10)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

        quests.add(new Quest(2, "Main quest 2", "Description for test quest 2.", "minecraft:stone", 0, 1, 1,
                List.of(new QuestObjective("kill", "minecraft:zombie", 10),
                        new QuestObjective("collect", "pathoscraft:raw_sadness", 5)),
                List.of(new QuestReward("give", "minecraft:gold_ingot", 1))
        ));

        quests.add(new Quest(3, "Main quest 3", "Description for test quest 3.", "minecraft:diamond_block", 0, 2, 2,
                List.of(new QuestObjective("kill", "minecraft:skeleton", 10),
                        new QuestObjective("collect", "pathoscraft:raw_sadness", 5)),
                List.of(new QuestReward("give", "minecraft:gold_ingot", 1))
        ));

        quests.add(new Quest(4, "Main quest 4", "Description for test quest 4.", "minecraft:apple", 0, 3, 3,
                List.of(new QuestObjective("kill", "minecraft:skeleton", 10),
                        new QuestObjective("collect", "minecraft:apple", 5)),
                List.of(new QuestReward("give", "minecraft:gold_ingot", 1))
        ));

        quests.add(new Quest(5, "Side Quest 1", "Description for test quest 5.", "minecraft:gold_block", 1, 0, 0,
                List.of(new QuestObjective("kill", "minecraft:ghast", 10),
                        new QuestObjective("collect", "minecraft:dirt", 5)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

        quests.add(new Quest(6, "Optional Quest 1", "Description for test quest 6.", "pathoscraft:raw_sadness", 2, 0, 0,
                List.of(new QuestObjective("kill", "minecraft:chicken", 10),
                        new QuestObjective("kill", "minecraft:pig", 5)),
                List.of(new QuestReward("give", "minecraft:diamond", 1))
        ));

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