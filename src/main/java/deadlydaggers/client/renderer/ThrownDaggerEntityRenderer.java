package deadlydaggers.client.renderer;

import deadlydaggers.entity.ThrownDaggerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class ThrownDaggerEntityRenderer extends EntityRenderer<ThrownDaggerEntity> {
    private static final Vector3f POSITIVE_Y = new Vector3f(0, 1, 0);
    private static final Vector3f POSITIVE_Z = new Vector3f(0, 0, 1);

    private static Quaternionf getDegreesQuaternion(Vector3f vector3f, float degrees) {
        float radians = (float) (degrees * Math.PI / 180);
        float f = (float) Math.sin(radians / 2.0f);

        return new Quaternionf(
            vector3f.x * f,
            vector3f.y * f,
            vector3f.z * f,
            Math.cos(radians / 2.0f)
        );
    }

   // public ThrownDaggerEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
   //     super(entityRenderDispatcher);
   // }
    public ThrownDaggerEntityRenderer(EntityRendererFactory.Context context){super(context);}

//rendering the item directly so we don't use this
    @Override
    public Identifier getTexture(ThrownDaggerEntity entity) {
        return null;
    }


    public void render(ThrownDaggerEntity daggerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(getDegreesQuaternion(POSITIVE_Y, daggerEntity.getYaw()-90));
        matrixStack.multiply(getDegreesQuaternion(POSITIVE_Z, daggerEntity.getPitch() - 85));
//spinning daggers in flight
       if(!daggerEntity.isInGround()){
        matrixStack.multiply(getDegreesQuaternion(POSITIVE_Y, daggerEntity.age*-20));
       }
        MinecraftClient.getInstance().getItemRenderer()
                .renderItem(daggerEntity.asItemStack(), ModelTransformationMode.THIRD_PERSON_RIGHT_HAND, i, 700000, matrixStack, vertexConsumerProvider, daggerEntity.getWorld(), 1);
        matrixStack.pop();
        super.render(daggerEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }
}
