package safro.auroral.client.render.block;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import safro.auroral.Auroral;
import safro.auroral.api.block.Beam;
import safro.auroral.block.entity.EnergyConnectableBlockEntity;
import safro.auroral.util.AuroralConstants;

import java.util.List;

public class EnergyConnectableBlockEntityRenderer<T extends EnergyConnectableBlockEntity> implements BlockEntityRenderer<T> {
    public static final Identifier BEAM_TEXTURE = new Identifier(Auroral.MODID, "textures/entity/beam.png");

    public EnergyConnectableBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
    }

    @Override
    public void render(T be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        List<Beam> beams = be.getBeams();
        long time = be.getWorld().getTime();
        if (!beams.isEmpty()) {
            for (Beam beam : beams) {
                Direction dir = beam.getDirection();
                matrices.push();
                if (dir == Direction.UP) {
                    renderBeam(matrices, vertexConsumers, tickDelta, time, 1, beam.getLength() - 1);
                } else if (dir == Direction.DOWN) {
                    matrices.translate(1.0, 0.0, 0.0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
                    renderBeam(matrices, vertexConsumers, tickDelta, time, 0, beam.getLength() - 1);
                } else if (dir == Direction.EAST) {
                    matrices.translate(1.0, 1.0, 0.0);
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(-90.0F));
                    renderBeam(matrices, vertexConsumers, tickDelta, time, 0, beam.getLength() - 1);
                } else if (dir == Direction.WEST) {
                    matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(90.0F));
                    renderBeam(matrices, vertexConsumers, tickDelta, time, 0, beam.getLength() - 1);
                } else if (dir == Direction.NORTH) {
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(-90.0F));
                    renderBeam(matrices, vertexConsumers, tickDelta, time, 0, beam.getLength() - 1);
                } else if (dir == Direction.SOUTH) {
                    matrices.translate(0.0, 1.0, 1.0);
                    matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
                    renderBeam(matrices, vertexConsumers, tickDelta, time, 0, beam.getLength() - 1);
                }
                matrices.pop();
            }
        }
    }

    private static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, float tickDelta, long worldTime, int yOffset, int maxY) {
        renderBeam(matrices, vertexConsumers, BEAM_TEXTURE, tickDelta, 1.0F, worldTime, yOffset, maxY, 0.15F, 0.2F);
    }

    public static void renderBeam(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier textureId, float tickDelta, float heightScale, long worldTime, int yOffset, int maxY, float innerRadius, float outerRadius) {
        int i = yOffset + maxY;
        matrices.push();
        matrices.translate(0.5, 0.0, 0.5);
        float f = (float)Math.floorMod(worldTime, 40) + tickDelta;
        float g = maxY < 0 ? f : -f;
        float h = MathHelper.fractionalPart(g * 0.2F - (float)MathHelper.floor(g * 0.1F));
        float j = AuroralConstants.BEAM_R;
        float k = AuroralConstants.BEAM_G;
        float l = AuroralConstants.BEAM_B;
        matrices.push();
        matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(f * 2.25F - 45.0F));
        float q = -innerRadius;
        float t = -innerRadius;
        float w = -1.0F + h;
        float x = (float)maxY * heightScale * (0.5F / innerRadius) + w;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, false)), j, k, l, 1.0F, yOffset, i, 0.0F, innerRadius, innerRadius, 0.0F, q, 0.0F, 0.0F, t, 0.0F, 1.0F, x, w);
        matrices.pop();
        float m = -outerRadius;
        float n = -outerRadius;
        float p = -outerRadius;
        q = -outerRadius;
        w = -1.0F + h;
        x = (float)maxY * heightScale + w;
        renderBeamLayer(matrices, vertexConsumers.getBuffer(RenderLayer.getBeaconBeam(textureId, true)), j, k, l, 0.125F, yOffset, i, m, n, outerRadius, p, q, outerRadius, outerRadius, outerRadius, 0.0F, 1.0F, x, w);
        matrices.pop();
    }

    private static void renderBeamLayer(MatrixStack matrices, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float x3, float z3, float x4, float z4, float u1, float u2, float v1, float v2) {
        MatrixStack.Entry entry = matrices.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        Matrix3f matrix3f = entry.getNormalMatrix();
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x1, z1, x2, z2, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x4, z4, x3, z3, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x2, z2, x4, z4, u1, u2, v1, v2);
        renderBeamFace(matrix4f, matrix3f, vertices, red, green, blue, alpha, yOffset, height, x3, z3, x1, z1, u1, u2, v1, v2);
    }

    private static void renderBeamFace(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int yOffset, int height, float x1, float z1, float x2, float z2, float u1, float u2, float v1, float v2) {
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x1, z1, u2, v1);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x1, z1, u2, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, yOffset, x2, z2, u1, v2);
        renderBeamVertex(positionMatrix, normalMatrix, vertices, red, green, blue, alpha, height, x2, z2, u1, v1);
    }

    private static void renderBeamVertex(Matrix4f positionMatrix, Matrix3f normalMatrix, VertexConsumer vertices, float red, float green, float blue, float alpha, int y, float x, float z, float u, float v) {
        vertices.vertex(positionMatrix, x, (float)y, z).color(1.0F, 1.0F, 1.0F, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).next();
    }
}
