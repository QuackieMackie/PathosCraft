package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.registers.PathosQuests;
import net.quackiemackie.pathoscraft.util.quest.Quest;

public class PathosLangProvider extends LanguageProvider {
    public PathosLangProvider(PackOutput output, String modid, String locale) {
        super(output, modid, locale);
    }

    @Override
    protected void addTranslations() {
        addStaticTranslations();
        addQuestTranslations();
    }


    private void addQuestTranslations() {
        this.add("quest.pathoscraft.objective.title", "§aObjectives:");
        this.add("quest.pathoscraft.reward.title", "§cRewards:");
        this.add("quest.pathoscraft.id", "§7Quest ID: ");
        this.add("quest.pathoscraft.type.main", "§7Type: §fMain Quest");
        this.add("quest.pathoscraft.type.side", "§7Type: §fSide Quest");
        this.add("quest.pathoscraft.type.optional", "§7Type: §fOptional Quest");
        this.add("quest.pathoscraft.type.unknown", "§7Type: §fUnknown");

        if (PathosQuests.getQuests() == null || PathosQuests.getQuests().isEmpty()) {
            PathosCraft.LOGGER.warn("PathosQuests.QUESTS is empty or null! No translations will be generated.");
            return;
        }

        for (Quest quest : PathosQuests.getQuests()) {
            PathosCraft.LOGGER.info("Processing quest: " + quest.getQuestId());
            String baseKey = "quest." + PathosCraft.MOD_ID + ".quest_" + quest.getQuestId();

            this.add(baseKey + ".name", "§7" + quest.getQuestName());
            this.add(baseKey + ".description", "§7" + quest.getQuestDescription());

            int index = 1;
            for (var objective : quest.getQuestObjectives()) {
                if ("collect".equalsIgnoreCase(objective.getAction())) {
                    String returnItems = "Returns";
                    if (!objective.getReturnItems()) {
                        returnItems = "Consumed";
                    }
                    this.add(baseKey + ".objective_" + index,
                            String.format("§8- §a%s %s 0/%d §8[§c%s§8]",
                                    objective.getAction(),
                                    translateTarget(objective.getTarget()),
                                    objective.getQuantity(),
                                    returnItems));
                } else {
                    this.add(baseKey + ".objective_" + index,
                            String.format("§8- §a%s %s 0/%d",
                                    objective.getAction(),
                                    translateTarget(objective.getTarget()),
                                    objective.getQuantity()));
                }
                index++;
            }

            index = 1;
            for (var reward : quest.getQuestRewards()) {
                this.add(baseKey + ".reward_" + index,
                        String.format("§8- §c%d %s",
                                reward.getQuantity(),
                                translateItemName(reward.getItem())));
                index++;
            }
        }
    }

    /**
     * Adds all static translations from the JSON file.
     */
    private void addStaticTranslations() {
        // Items
        this.add("item.pathoscraft.raw_sadness", "Raw Sadness");
        this.add("item.pathoscraft.sadness_ingot", "Sadness Ingot");
        this.add("item.pathoscraft.raw_sunny", "Raw Sunny");
        this.add("item.pathoscraft.sunny_ingot", "Sunny Ingot");
        this.add("item.pathoscraft.jump_wand", "Jump Wand");
        this.add("item.pathoscraft.mana_herb", "Mana Herb");
        this.add("item.pathoscraft.mana_potion", "Mana Potion");
        this.add("item.pathoscraft.arrow_test", "Test Arrow");

        // Sadness tools
        this.add("item.pathoscraft.sadness_sword", "Sadness Sword");
        this.add("item.pathoscraft.sadness_pickaxe", "Sadness Pickaxe");
        this.add("item.pathoscraft.sadness_axe", "Sadness Axe");
        this.add("item.pathoscraft.sadness_shovel", "Sadness Shovel");
        this.add("item.pathoscraft.sadness_hoe", "Sadness Hoe");
        this.add("item.pathoscraft.basic_fishing_rod", "Basic Fishing Rod");

        // Other Items
        this.add("item.pathoscraft.animated_item", "Test Animated Item");
        this.add("item.pathoscraft.astral_lantern", "Astral Lantern");
        this.add("item.pathoscraft.quest_book", "§dQuest Book");
        this.add("item.pathoscraft.creature_crystal", "Creature Crystal");
        this.add("item.pathoscraft.test_item", "Test Item");

        // Blocks
        this.add("block.pathoscraft.sadness_block", "Block of Sadness");
        this.add("block.pathoscraft.sadness_ore", "Sadness Ore");
        this.add("block.pathoscraft.deepslate_sadness_ore", "Deepslate Sadness Ore");
        this.add("block.pathoscraft.sunny_ore", "Sunny Ore");
        this.add("block.pathoscraft.deepslate_sunny_ore", "Deepslate Sunny Ore");
        this.add("block.pathoscraft.repair_block", "Repair Block");

        // Enchantments
        this.add("enchantment.pathoscraft.minigame", "Minigame");

        // Messages
        this.add("message.pathoscraft.minigame_status", "§8Minigame Status [%s§8]");
        this.add("message.pathoscraft.status.active", "§cEnabled");
        this.add("message.pathoscraft.status.inactive", "§cDisabled");

        // Creative tabs
        this.add("creative_tab.pathoscraft.blocks", "Building Blocks");
        this.add("creative_tab.pathoscraft.ingredients", "Ingredients");
        this.add("creative_tab.pathoscraft.tools", "Tools");
        this.add("creative_tab.pathoscraft.food", "Food");
        this.add("creative_tab.pathoscraft.misc", "Misc");

        // Item tooltips
        this.add("item.pathoscraft.jump_wand.tooltip", "Click the ground to teleport to that block!");
        this.add("item.pathoscraft.mana_potion.tooltip", "Drinking this gives you regeneration II for 5 seconds!");
        this.add("item.pathoscraft.mana_herb.tooltip", "Harvested in the open world, this herb when eaten has a chance to heal you.\nIt's also pretty good for cooking with...");
        this.add("item.pathoscraft.quest_book.tooltip", "§8A log of quests available and active for you.\n\n§7§oLegend has it...\n§7§oThis book contains the power to grant §6mythical abilities§7§o by fulfilling its tasks.");

        // Block tooltips
        this.add("block.pathoscraft.repair_block.tooltip", "Repair your jump wand by:\n- §cDropping your jump wand on the block!\n§r- §cClicking the repair block with your jump wand!");

        // Key Bindings
        this.add("keybinding.pathoscraft.category", "PathosCraft Key binds");
        this.add("keybinding.pathoscraft.astral_form_exit", "Exit Astral Form");

        // Abilities
        this.add("abilities.pathoscraft.astral_form_exit", "Press %s to exit Astral Form");
        this.add("abilities.pathoscraft.astral_form_name", "'s Physical body");

        // Networking
        this.add("networking.pathoscraft.client.failed", "Client network error:");
        this.add("networking.pathoscraft.server.failed", "Server network error:");

        // Quest Menu Screen
        this.add("screen.title.pathoscraft.quest_menu", "Quest Menu");
        this.add("screen.widget.pathoscraft.quest_menu.main_quest_button", "Main Quests");
        this.add("screen.widget.pathoscraft.quest_menu.side_quest_button", "Side Quests");
        this.add("screen.widget.pathoscraft.quest_menu.optional_quest_button", "Optional Quests");
        this.add("screen.widget.pathoscraft.quest_menu.active_quest_button", "Active Quests");
        this.add("screen.widget.pathoscraft.quest_menu.information_button.tooltip", "§5Quest System Guide:\n§aGreen: Quests with completed objectives.\n§6Orange: Quests on your active list.\n§8Grey: Quests that have been completed.\n\n§5Controls:\n• §7Left-click: Claim completed quests rewards and activate quests.\n• §7Right-click: Disable active quests.\n• §7Middle-click: Swap quests.\n\nTip: Hover over quests for details!");
        this.add("screen.widget.pathoscraft.quest_menu.page_number", "Page %1$s/%2$s");

        // Fishing Minigame
        this.add("screen.title.pathoscraft.fishing_mini_game", "Fishing Mini Game");
        this.add("screen.widget.pathoscraft.fishing_mini_game.match_key", "Match this key >");
        this.add("screen.widget.pathoscraft.fishing_mini_game.score", "Score: %s");
        this.add("screen.widget.pathoscraft.fishing_mini_game.round", "Round: %s");
        this.add("screen.widget.pathoscraft.fishing_mini_game.quit_early", "You quit early, final score: %s");
        this.add("screen.widget.pathoscraft.fishing_mini_game.success", "You've won! Total score: %s");
        this.add("screen.widget.pathoscraft.fishing_mini_game.failure", "Game over! Total score: %s");
        this.add("screen.widget.pathoscraft.fishing_mini_game.start_message", "Press any key to start!");
        this.add("screen.widget.pathoscraft.fishing_mini_game.instructions", "§5Fishing Mini-Game Instructions§r\n§aGoal: Match the sequence of keys displayed on the screen to catch the fish!§r\n§6Each correct key press brings you closer to completing the round.§r\n§8Each round gets progressively harder with longer sequences.§r\n\n§5How to Play:§r\n§7Start: Press any key to begin the game.§r\n§7Key Matching: Pay attention to the displayed sequence. Press the correct keys in order to advance.§r\n\n§2Scoring:§r\n§7Earn points for every correct match, try to beat your high score!");

        // Excavation Minigame
        this.add("screen.title.pathoscraft.excavation_mini_game", "Excavation Mini-Game");
        this.add("screen.widget.pathoscraft.excavation_mini_game.quit_early", "You quit early! Ores found: %s");
        this.add("screen.widget.pathoscraft.excavation_mini_game.congratulations", "Congratulations, you won! Total ores found: %s");
        this.add("screen.widget.pathoscraft.excavation_mini_game.better_luck", "Better luck next time! Total ores found: %s");
        this.add("screen.widget.pathoscraft.excavation_mini_game.lives", "Lives: %s");
        this.add("screen.widget.pathoscraft.excavation_mini_game.amazing", "Amazing! You found %s veins of ore!");
        this.add("screen.widget.pathoscraft.excavation_mini_game.not_bad", "Not bad, you found %s veins of ore!");
        this.add("screen.widget.pathoscraft.excavation_mini_game.better_luck_next_time", "Better luck next time!");
        this.add("screen.widget.pathoscraft.excavation_mini_game.exit_prompt", "Press '%s' or 'ESC' to exit and claim your rewards.");
        this.add("screen.widget.pathoscraft.excavation_mini_game.instructions", "§5Excavation Mini-Game Instructions§r\n§aGoal: Uncover valuable rewards by mining squares on the grid!§r\n§6Be strategic: Each square could contain a hidden reward or nothing at all.§r\n§8Your pickaxes represent your lives—run out of pickaxes, and the game ends.§r\n\n§5How to Play:§r\n§7Grid: Click squares on the grid to mine.§r\n§7Rewards: Uncover hidden rewards like ore or treasure to collect them.§r\n§7Empty Squares: If the square is empty, your pickaxe breaks and you lose a life.§r\n\n§2Rewards:§r\n§7Keep whatever rewards you uncover.§r");
    }

    private static String translateTarget(String target) {
        return target.substring(target.indexOf(':') + 1).replace('_', ' ');
    }

    private static String translateItemName(String itemName) {
        return itemName.substring(itemName.indexOf(':') + 1).replace('_', ' ');
    }
}
