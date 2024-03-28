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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoisonDaggerRecipe extends SpecialCraftingRecipe {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadlyDaggers.MODID);
    private static void copyNbt(NbtCompound source, NbtCompound target, String key) {
        if (source.contains(key)) {
            target.put(key, source.get(key));
        }
    }

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
            else if (!stack.isEmpty()) return false;
        }

        LOGGER.info("{}, {}", hasPotion, hasDagger);

        return hasPotion && hasDagger;
    }

    @Override
    public ItemStack craft(RecipeInputInventory inventory, DynamicRegistryManager registryManager) {
        ItemStack dagger = null;
        ItemStack potion = null;

        for (ItemStack stack : inventory.getInputStacks()) {
            if (stack.getItem() instanceof DaggerItem) dagger = stack;
            else if (stack.isOf(Items.POTION)) potion = stack;
        }

        if (dagger == null || potion == null) return ItemStack.EMPTY;

        NbtCompound potionNbt = potion.getNbt();

        if (potionNbt == null) return ItemStack.EMPTY;

        ItemStack outputDagger = dagger.copy();
        NbtCompound daggerNbt = outputDagger.getOrCreateNbt();

        copyNbt(potionNbt, daggerNbt, "Potion");
        copyNbt(potionNbt, daggerNbt, "CustomPotionEffects");
        copyNbt(potionNbt, daggerNbt, "CustomPotionColor");

        LOGGER.info("{}", outputDagger);

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
