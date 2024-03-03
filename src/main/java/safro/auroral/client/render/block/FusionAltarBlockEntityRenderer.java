package safro.auroral.client.render.block;

import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.RotationAxis;
import safro.auroral.block.entity.FusionAltarBlockEntity;
import safro.auroral.config.AuroralConfig;

public class FusionAltarBlockEntityRenderer extends EnergyConnectableBlockEntityRenderer<FusionAltarBlockEntity> {

    public FusionAltarBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Override
    public void render(FusionAltarBlockEntity be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        super.render(be, tickDelta, matrices, vertexConsumers, light, overlay);
        Direction dir = be.getCachedState().get(HorizontalFacingBlock.FACING);
        Direction.Axis axis = dir.getAxis();
        int k = (int)be.getPos().asLong();

        for (int i = 0; i < be.size(); i++) {
            ItemStack stack = be.getItems().get(i);
            if (!stack.isEmpty()) {
                matrices.push();
                float offset = 0.0F;
                if (be.isCrafting()) {
                    offset = 0.0035F * (AuroralConfig.get().fusionCraftTime - be.getCraftTicks());
                }

                if (i == 2) {
                    matrices.translate(0.5F, 1.01F, 0.5F);
                } else if (axis == Direction.Axis.X) {
                    matrices.translate(0.5F, 1.01F, i == 0 ? 0.15F + offset : 0.85F - offset);
                } else {
                    matrices.translate(i == 0 ? 0.15F + offset : 0.85F - offset, 1.01F, 0.5F);
                }
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                if (axis == Direction.Axis.Z) {
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
                }
                matrices.scale(0.4F, 0.4F, 0.4F);
                int blockLight = WorldRenderer.getLightmapCoordinates(be.getWorld(), be.getCachedState(), be.getPos().offset(dir));
                MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.FIXED, blockLight, overlay, matrices, vertexConsumers, be.getWorld(), k + i);
                matrices.pop();
            }
        }
    }
}
