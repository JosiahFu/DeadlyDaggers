package deadlydaggers.recipe;

import deadlydaggers.DeadlyDaggers;
import deadlydaggers.item.DaggerItem;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class PoisonDaggerRecipe extends SpecialCraftingRecipe {
    public PoisonDaggerRecipe(Identifier id, CraftingRecipeCategory category) {
        super(id, category);
    }

    @Override
    public boolean matches(RecipeInputInventory inventory, World world) {
        boolean hasDagger = false;
        boolean hasPotion = false;

        for (ItemStack stack : inventory.getInputStacks()) {
            if (!hasDagger && stack.getItem() instanceof DaggerItem) hasDagger = true;
            else if (!hasPotion && stack.isOf(Items.POTION)) hasPotion = true;
            else return false;
        }

        return (hasPotion && hasDagger);
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack dagger = null;
        ItemStack potion = null;

        for (ItemStack stack : inventory.getInputStacks()) {
            if (stack.getItem() instanceof DaggerItem) dagger = stack;
            else if (stack.isOf(Items.POTION)) potion = stack;
        }

        if (dagger == null || potion == null) return null;

        NbtCompound potionNbt = potion.getNbt();

        if (potionNbt == null) return null;

        ItemStack outputDagger = dagger.copy();
        NbtCompound daggerNbt = outputDagger.getOrCreateNbt();
        daggerNbt.put("Potion", potionNbt.get("Potion"));
        daggerNbt.put("CustomPotionEffects", potionNbt.get("CustomPotionEffects"));
        daggerNbt.put("CustomPotionColor", potionNbt.get("CustomPotionColor"));

        return outputDagger;
    }

    @Override
    public boolean fits(int width, int height) {
        return width * height >= 2;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return DeadlyDaggers.POISON_DAGGER_RECIPE;
    }
}
