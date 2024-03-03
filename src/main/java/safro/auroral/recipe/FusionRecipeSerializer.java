package safro.auroral.recipe;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.RecipeSerializer;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;

public class FusionRecipeSerializer implements RecipeSerializer<FusionRecipe> {

    @Override
    public FusionRecipe read(Identifier id, JsonObject jsonObject) {
        JsonElement jsonElement = JsonHelper.hasArray(jsonObject, "input1") ? JsonHelper.getArray(jsonObject, "input1") : JsonHelper.getObject(jsonObject, "input1");
        Ingredient first = Ingredient.fromJson(jsonElement, false);
        JsonElement jsonElement2 = JsonHelper.hasArray(jsonObject, "input2") ? JsonHelper.getArray(jsonObject, "input2") : JsonHelper.getObject(jsonObject, "input2");
        Ingredient second = Ingredient.fromJson(jsonElement2, false);
        String string2 = JsonHelper.getString(jsonObject, "result");
        Identifier identifier2 = new Identifier(string2);
        ItemStack itemStack = new ItemStack(Registries.ITEM.getOrEmpty(identifier2).orElseThrow(() -> new IllegalStateException("Item: " + string2 + " does not exist")));
        return new FusionRecipe(id, first, second, itemStack);
    }

    @Override
    public FusionRecipe read(Identifier id, PacketByteBuf buf) {
        return new FusionRecipe(id, Ingredient.fromPacket(buf), Ingredient.fromPacket(buf), buf.readItemStack());
    }

    @Override
    public void write(PacketByteBuf buf, FusionRecipe recipe) {
        recipe.first.write(buf);
        recipe.second.write(buf);
        buf.writeItemStack(recipe.output);
    }
}
