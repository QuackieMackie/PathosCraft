package net.quackiemackie.pathoscraft.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;
import net.quackiemackie.pathoscraft.PathosCraft;
import net.quackiemackie.pathoscraft.block.ModBlocks;
import net.quackiemackie.pathoscraft.item.ModItems;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput recipeOutput) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.SADNESS_BLOCK.get())
                .pattern("BBB")
                .pattern("BBB")
                .pattern("BBB")
                .define('B', ModItems.SADNESS_INGOT.get())
                .unlockedBy("has_sadness_ingot", has(ModItems.SADNESS_INGOT.get())).save(recipeOutput, "pathoscraft:sadness_block_from_sadness_ingot");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.SADNESS_INGOT.get(), 9)
                .requires(ModBlocks.SADNESS_BLOCK)
                .unlockedBy("has_sadness_block", has(ModBlocks.SADNESS_BLOCK)).save(recipeOutput, "pathoscraft:sadness_ingot_from_sadness_block");

        List<ItemLike> SADNESS_INGOTS_SMELTABLES = List.of(ModItems.RAW_SADNESS.get(), ModBlocks.SADNESS_ORE.get(), ModBlocks.DEEPSLATE_SADNESS_ORE.get());
        oreSmelting(recipeOutput, SADNESS_INGOTS_SMELTABLES, RecipeCategory.MISC, ModItems.SADNESS_INGOT.get(), 0.25f, 200, "sadness_ingot");
        oreBlasting(recipeOutput, SADNESS_INGOTS_SMELTABLES, RecipeCategory.MISC, ModItems.SADNESS_INGOT.get(), 0.40f, 100, "sadness_ingot");

        List<ItemLike> SUNNY_INGOTS_SMELTABLES = List.of(ModItems.RAW_SUNNY.get(), ModBlocks.SUNNY_ORE.get(), ModBlocks.DEEPSLATE_SUNNY_ORE.get());
        oreSmelting(recipeOutput, SUNNY_INGOTS_SMELTABLES, RecipeCategory.MISC, ModItems.SUNNY_INGOT.get(), 0.25f, 200, "sunny_ingot");
        oreBlasting(recipeOutput, SUNNY_INGOTS_SMELTABLES, RecipeCategory.MISC, ModItems.SUNNY_INGOT.get(), 0.40f, 100, "sunny_ingot");
    }

    protected static void oreSmelting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTIme, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTIme, pGroup, "_from_smelting");
    }

    protected static void oreBlasting(RecipeOutput recipeOutput, List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult,
                                      float pExperience, int pCookingTime, String pGroup) {
        oreCooking(recipeOutput, RecipeSerializer.BLASTING_RECIPE, BlastingRecipe::new, pIngredients, pCategory, pResult,
                pExperience, pCookingTime, pGroup, "_from_blasting");
    }

    protected static <T extends AbstractCookingRecipe> void oreCooking(RecipeOutput recipeOutput, RecipeSerializer<T> pCookingSerializer, AbstractCookingRecipe.Factory<T> factory,
                                                                       List<ItemLike> pIngredients, RecipeCategory pCategory, ItemLike pResult, float pExperience, int pCookingTime, String pGroup, String pRecipeName) {
        for(ItemLike itemlike : pIngredients) {
            SimpleCookingRecipeBuilder.generic(Ingredient.of(itemlike), pCategory, pResult, pExperience, pCookingTime, pCookingSerializer, factory).group(pGroup).unlockedBy(getHasName(itemlike), has(itemlike))
                    .save(recipeOutput, PathosCraft.MOD_ID + ":" + getItemName(pResult) + pRecipeName + "_" + getItemName(itemlike));
        }
    }
}
