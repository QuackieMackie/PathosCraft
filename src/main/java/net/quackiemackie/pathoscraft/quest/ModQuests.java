package net.quackiemackie.pathoscraft.quest;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.registers.RegisterRegistry;

import java.util.List;
import java.util.function.Supplier;

public class ModQuests {

    public static final DeferredRegister<Quest> QUESTS = DeferredRegister.create(RegisterRegistry.QUEST_REGISTRY_KEY, PathosCraft.MOD_ID);

    public static final Supplier<Quest> TEST_QUEST = QUESTS.register("test_quest", () -> new Quest(
            1,
            "Test Name",
            "A test quest.",
            1,
            List.of(
                    new QuestObjective("gather", "minecraft:dirt", 10)
            ),
            List.of(
                    new QuestReward("give", "minecraft:diamond", 1)
            )));

    public static void register(IEventBus modEventBus){
        QUESTS.register(modEventBus);
    }
}