package deadlydaggers.recipe;

import deadlydaggers.DeadlyDaggers;
import deadlydaggers.item.DaggerItem;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.PotionUtil;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.SpecialCraftingRecipe;
import net.minecraft.recipe.book.CraftingRecipeCategory;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PoisonDaggerRecipe extends SpecialCraftingRecipe {
    private static final Logger LOGGER = LoggerFactory.getLogger(DeadlyDaggers.MODID);

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
        ItemStack potionStack = null;

        for (ItemStack stack : inventory.getInputStacks()) {
            if (stack.getItem() instanceof DaggerItem) dagger = stack;
            else if (stack.isOf(Items.POTION)) potionStack = stack;
        }

        if (dagger == null || potionStack == null) return ItemStack.EMPTY;

        ItemStack outputDagger = dagger.copy();

        PotionUtil.setPotion(outputDagger, PotionUtil.getPotion(potionStack));

        return outputDagger;
    }

    @Override
    public DefaultedList<ItemStack> getRemainder(RecipeInputInventory inventory) {
        DefaultedList<ItemStack> defaultedList = DefaultedList.ofSize(inventory.size(), ItemStack.EMPTY);

        for(int i = 0; i < defaultedList.size(); ++i) {
            Item item = inventory.getStack(i).getItem();
            if (item.getRecipeRemainder() != null) {
                defaultedList.set(i, new ItemStack(item.getRecipeRemainder()));
            } else if (item instanceof PotionItem) {
                defaultedList.set(i, new ItemStack(Items.GLASS_BOTTLE));
            }
        }

        return defaultedList;
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
