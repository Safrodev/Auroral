package safro.auroral.recipe;

import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.Recipe;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import safro.auroral.registry.RecipeRegistry;

public class FusionRecipe implements Recipe<Inventory> {
    protected final Ingredient first;
    protected final Ingredient second;
    protected final ItemStack output;
    protected final Identifier id;

    public FusionRecipe(Identifier id, Ingredient input1, Ingredient input2, ItemStack output) {
        this.first = input1;
        this.second = input2;
        this.output = output;
        this.id = id;
    }

    @Override
    public boolean matches(Inventory inventory, World world) {
        return (this.first.test(inventory.getStack(0)) && this.second.test(inventory.getStack(1))) || (this.first.test(inventory.getStack(1)) && this.second.test(inventory.getStack(0)));
    }

    @Override
    public ItemStack craft(Inventory inventory, DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    @Override
    public boolean fits(int width, int height) {
        return true;
    }

    @Override
    public ItemStack getOutput(DynamicRegistryManager registryManager) {
        return this.output.copy();
    }

    @Override
    public Identifier getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return RecipeRegistry.FUSION_SERIALIZER;
    }

    @Override
    public RecipeType<?> getType() {
        return RecipeRegistry.FUSION;
    }
}
