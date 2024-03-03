package safro.auroral.registry;

import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.recipe.RecipeType;
import safro.auroral.Auroral;
import safro.auroral.recipe.FusionRecipe;
import safro.auroral.recipe.FusionRecipeSerializer;
import safro.saflib.registry.BaseRecipeRegistry;

public class RecipeRegistry extends BaseRecipeRegistry {
    static { MODID = Auroral.MODID; }

    public static final RecipeType<FusionRecipe> FUSION = register("fusion");
    public static final RecipeSerializer<FusionRecipe> FUSION_SERIALIZER = register("fusion", new FusionRecipeSerializer());

    public static void init() {}
}
