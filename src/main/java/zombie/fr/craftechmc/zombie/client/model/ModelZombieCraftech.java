package fr.craftechmc.zombie.client.model;

import static fr.craftechmc.zombie.client.model.ModelZombieCraftech.Rotation.*;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;

/**
 * Created by Phenix246 on 22/06/2016.
 */
public class ModelZombieCraftech extends ModelBase
{
    public enum Rotation
    {
        HEAD(0F, 0F, 0F), LEFT_ARM(5F, 2F, 0F), RIGHT_ARM(-5F, 2F, 0F), LEFT_LEG(2F, 12F, 0F), RIGHT_LEG(-2F, 12F, 0F);

        private final float x, y, z;

        Rotation(float x, float y, float z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public float getX()
        {
            return this.x;
        }

        public float getY()
        {
            return this.y;
        }

        public float getZ()
        {
            return this.z;
        }

    };

    public static final float PI   = (float) Math.PI;
    public static final float NPI2 = -ModelZombieCraftech.PI / 2;

    // Head
    ModelRenderer             helmet;
    ModelRenderer             head;
    ModelRenderer             brain;
    ModelRenderer             skullFront;
    ModelRenderer             skullBack;

    // Full Arm
    ModelRenderer             fullArm;

    // Bone Arm
    ModelRenderer             boneArmFlesh;
    ModelRenderer             boneArmBoneArm;
    ModelRenderer             boneArmBoneHand;

    // Full Leg
    ModelRenderer             fullLeg;

    // Bone Leg
    ModelRenderer             boneLegFlesh;
    ModelRenderer             boneLegBoneLeg;
    ModelRenderer             boneLegBoneFoot;

    // Body
    ModelRenderer             body;
    ModelRenderer             cote_1_1;
    ModelRenderer             cote_1_2;
    ModelRenderer             cote_1_3;
    ModelRenderer             cote_2_1;
    ModelRenderer             cote_2_2;
    ModelRenderer             cote_2_3;
    ModelRenderer             cote_3_1;
    ModelRenderer             cote_3_2;
    ModelRenderer             cote_3_3;
    ModelRenderer             cote_4_1;
    ModelRenderer             cote_4_2;
    ModelRenderer             cote_4_3;
    ModelRenderer             cote_5_1;
    ModelRenderer             cote_5_2;
    ModelRenderer             cote_5_3;
    ModelRenderer             cote_6_1;
    ModelRenderer             cote_6_2;
    ModelRenderer             cote_6_3;
    ModelRenderer             column;

    public ModelZombieCraftech()
    {
        this(true, true);
    }

    public ModelZombieCraftech(boolean leftArm, boolean leftLeg)
    {
        this(HEAD, leftArm ? LEFT_ARM : RIGHT_ARM, leftArm ? RIGHT_ARM : LEFT_ARM, leftLeg ? LEFT_LEG : RIGHT_LEG,
                leftLeg ? RIGHT_LEG : LEFT_LEG);
    }

    public ModelZombieCraftech(Rotation head, Rotation boneArm, Rotation fullArm, Rotation boneLeg, Rotation fullLeg)
    {
        // Head
        this.helmet = this.createModel(32, 0);
        this.helmet.addBox(-4F, -8F, -4F, 8, 8, 8);
        this.helmet.setRotationPoint(head.getX(), head.getY(), head.getZ());

        this.head = this.createModel(0, 0);
        this.head.addBox(-4F, -8F, -4F, 8, 8, 8);
        this.head.setRotationPoint(head.getX(), head.getY(), head.getZ());

        this.brain = this.createModel(4, 41);
        this.brain.addBox(-3.5F, -7.5F, -3.5F, 7, 7, 7);
        this.brain.setRotationPoint(head.getX(), head.getY(), head.getZ());

        this.skullFront = this.createModel(6, 55);
        this.skullFront.addBox(-3.5F, -4.01F, -3.5F, 4, 4, 5);
        this.skullFront.setRotationPoint(head.getX(), head.getY(), head.getZ());

        this.skullBack = this.createModel(6, 55);
        this.skullBack.addBox(-0.5F, -4F, -1.5F, 4, 4, 5);
        this.skullBack.setRotationPoint(head.getX(), head.getY(), head.getZ());

        // Full Arm
        this.fullArm = this.createModel(48, 41);
        this.fullArm.addBox(-3F, -2F, -2F, 4, 12, 4);
        this.fullArm.setRotationPoint(fullArm.getX(), fullArm.getY(), fullArm.getZ());

        // Bone Arm
        this.boneArmFlesh = this.createModel(40, 16);
        this.boneArmFlesh.addBox(-1F, -2F, -2F, 4, 9, 4);
        this.boneArmFlesh.setRotationPoint(boneArm.getX(), boneArm.getY(), boneArm.getZ());

        this.boneArmBoneArm = this.createModel(0, 32);
        this.boneArmBoneArm.addBox(0.5F, -0.5F, 1.5F, 1, 1, 8);
        this.boneArmBoneArm.setRotationPoint(boneArm.getX(), boneArm.getY(), boneArm.getZ());

        this.boneArmBoneHand = this.createModel(37, 36);
        this.boneArmBoneHand.addBox(0F, -1F, 8.5F, 2, 2, 1);
        this.boneArmBoneHand.setRotationPoint(boneArm.getX(), boneArm.getY(), boneArm.getZ());

        // Full Leg
        this.fullLeg = this.createModel(0, 16);
        this.fullLeg.addBox(-2F, 0F, -2F, 4, 12, 4);
        this.fullLeg.setRotationPoint(fullLeg.getX(), fullLeg.getY(), fullLeg.getZ());

        // Bone Leg
        this.boneLegFlesh = this.createModel(32, 41);
        this.boneLegFlesh.addBox(-2F, 0F, -2F, 4, 9, 4);
        this.boneLegFlesh.setRotationPoint(boneLeg.getX(), boneLeg.getY(), boneLeg.getZ());

        this.boneLegBoneLeg = this.createModel(0, 32);
        this.boneLegBoneLeg.addBox(-0.5F, -0.5F, 3.5F, 1, 1, 8);
        this.boneLegBoneLeg.setRotationPoint(boneLeg.getX(), boneLeg.getY(), boneLeg.getZ());

        this.boneLegBoneFoot = this.createModel(37, 36);
        this.boneLegBoneFoot.addBox(-1F, -0.7F, 11F, 2, 2, 1);
        this.boneLegBoneFoot.setRotationPoint(boneLeg.getX(), boneLeg.getY(), boneLeg.getZ());

        // Body
        this.body = this.createModel(16, 16);
        this.body.addBox(-4F, -6F, -2F, 8, 12, 4);
        this.body.setRotationPoint(0F, 6F, 0F);

        this.cote_1_1 = this.createModel(0, 42);
        this.cote_1_1.addBox(-0.5F, -6F, -0.5F, 1, 12, 1);
        this.cote_1_1.setRotationPoint(0F, 6F, 1F);
        this.cote_1_2 = this.createModel(0, 42);
        this.cote_1_2.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_1_2.setRotationPoint(-1.7F, 1F, 0F);
        this.cote_1_3 = this.createModel(0, 42);
        this.cote_1_3.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_1_3.setRotationPoint(1.7F, 1F, 0F);
        this.cote_2_1 = this.createModel(0, 42);
        this.cote_2_1.addBox(-0.5F, -3F, -0.5F, 1, 6, 1);
        this.cote_2_1.setRotationPoint(0F, 3F, 1F);
        this.cote_2_2 = this.createModel(0, 42);
        this.cote_2_2.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_2_2.setRotationPoint(-2.2F, 3F, 0F);
        this.cote_2_3 = this.createModel(0, 42);
        this.cote_2_3.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_2_3.setRotationPoint(2.2F, 3F, 0F);
        this.cote_3_1 = this.createModel(0, 42);
        this.cote_3_1.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1);
        this.cote_3_1.setRotationPoint(0F, 5F, 1F);
        this.cote_3_2 = this.createModel(0, 42);
        this.cote_3_2.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_3_2.setRotationPoint(-2.7F, 5F, 0F);
        this.cote_3_3 = this.createModel(0, 42);
        this.cote_3_3.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_3_3.setRotationPoint(2.7F, 5F, 0F);
        this.cote_4_1 = this.createModel(0, 42);
        this.cote_4_1.addBox(-0.5F, -3.5F, -0.5F, 1, 7, 1);
        this.cote_4_1.setRotationPoint(0F, 7F, 1F);
        this.cote_4_2 = this.createModel(0, 42);
        this.cote_4_2.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_4_2.setRotationPoint(-2.7F, 7F, 0F);
        this.cote_4_3 = this.createModel(0, 42);
        this.cote_4_3.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_4_3.setRotationPoint(2.7F, 7F, 0F);
        this.cote_5_1 = this.createModel(0, 42);
        this.cote_5_1.addBox(-0.5F, -3F, -0.5F, 1, 6, 1);
        this.cote_5_1.setRotationPoint(0F, 9F, 1F);
        this.cote_5_2 = this.createModel(0, 42);
        this.cote_5_2.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_5_2.setRotationPoint(-2.2F, 9F, 0F);
        this.cote_5_3 = this.createModel(0, 42);
        this.cote_5_3.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_5_3.setRotationPoint(2.2F, 9F, 0F);
        this.cote_6_1 = this.createModel(0, 42);
        this.cote_6_1.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1);
        this.cote_6_1.setRotationPoint(0F, 11F, 1F);
        this.cote_6_2 = this.createModel(0, 42);
        this.cote_6_2.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_6_2.setRotationPoint(-1.7F, 11F, 0F);
        this.cote_6_3 = this.createModel(0, 42);
        this.cote_6_3.addBox(-0.5F, -1F, -0.5F, 1, 2, 1);
        this.cote_6_3.setRotationPoint(1.7F, 11F, 0F);
        this.column = this.createModel(0, 42);
        this.column.addBox(-0.5F, -2.5F, -0.5F, 1, 5, 1);
        this.column.setRotationPoint(0F, 1F, 1F);
    }

    public void renderHead(Entity entity, float angleX, float angleY, float angleZ, float scale)
    {
        this.setRotation(this.helmet, angleX, angleY, angleZ);
        this.helmet.renderWithRotation(scale);

        this.setRotation(this.head, angleX, angleY, angleZ);
        this.head.renderWithRotation(scale);

        this.setRotation(this.brain, angleX, angleY, angleZ);
        this.brain.renderWithRotation(scale);

        this.setRotation(this.skullFront, angleX, angleY, angleZ);
        this.skullFront.renderWithRotation(scale);

        this.setRotation(this.skullBack, angleX, angleY, angleZ);
        this.skullBack.renderWithRotation(scale);
    }

    public void renderFullArm(Entity entity, float angleX, float angleY, float angleZ, float scale)
    {
        this.setRotation(this.fullArm, angleX + ModelZombieCraftech.NPI2, angleY, angleZ);
        this.fullArm.renderWithRotation(scale);
    }

    public void renderBoneArm(Entity entity, float angleX, float angleY, float angleZ, float scale)
    {
        // this.boneArm.rotateAngleX = 1.570796F;

        this.setRotation(this.boneArmFlesh, angleX + ModelZombieCraftech.NPI2, angleY, angleZ);
        this.boneArmFlesh.renderWithRotation(scale);

        // this.boneArmBoneArm.rotateAngleX = 0.01940952F;

        this.setRotation(this.boneArmBoneArm, angleX + ModelZombieCraftech.PI, angleY, angleZ);
        this.boneArmBoneArm.renderWithRotation(scale);

        // this.boneArmBoneHand.rotateAngleX = 0.01940952F;

        this.setRotation(this.boneArmBoneHand, angleX + ModelZombieCraftech.PI, angleY, angleZ);
        this.boneArmBoneHand.renderWithRotation(scale);
    }

    public void renderFullLeg(Entity entity, float angleX, float angleY, float angleZ, float scale)
    {
        this.setRotation(this.fullLeg, angleX, angleY, angleZ);
        this.fullLeg.renderWithRotation(scale);
    }

    public void renderBoneLeg(Entity entity, float angleX, float angleY, float angleZ, float scale)
    {
        this.setRotation(this.boneLegFlesh, angleX, 0F, 0F);
        this.boneLegFlesh.renderWithRotation(scale);

        this.setRotation(this.boneLegBoneLeg, ModelZombieCraftech.NPI2 + angleX, 0F, 0F);
        this.boneLegBoneLeg.renderWithRotation(scale);

        this.setRotation(this.boneLegBoneFoot, ModelZombieCraftech.NPI2 + angleX, 0F, 0F);
        this.boneLegBoneFoot.renderWithRotation(scale);
    }

    public void renderBody(Entity entity, float angleX, float angleY, float angleZ, float scale)
    {
        final float aX1 = 1.605692E-08F;
        final float aY1 = 2.007128F;
        final float aY2 = 1.134464F;

        this.setRotation(this.body, 0F, 0F, 0F);
        this.body.renderWithRotation(scale);

        this.setRotation(this.cote_1_1, 0F, 0F, 0F);
        this.cote_1_1.renderWithRotation(scale);

        this.setRotation(this.cote_1_2, 0F, aY2, ModelZombieCraftech.NPI2);
        this.cote_1_2.renderWithRotation(scale);

        this.setRotation(this.cote_1_3, aX1, aY1, ModelZombieCraftech.NPI2);
        this.cote_1_3.renderWithRotation(scale);

        this.setRotation(this.cote_2_1, 0F, 0F, ModelZombieCraftech.NPI2);
        this.cote_2_1.renderWithRotation(scale);

        this.setRotation(this.cote_2_2, -4.529097E-08F, aY2, ModelZombieCraftech.NPI2);
        this.cote_2_2.renderWithRotation(scale);

        this.setRotation(this.cote_2_3, -aX1, aY1, ModelZombieCraftech.NPI2);
        this.cote_2_3.renderWithRotation(scale);

        this.setRotation(this.cote_3_1, 0F, 0F, ModelZombieCraftech.NPI2);
        this.cote_3_1.renderWithRotation(scale);

        this.setRotation(this.cote_3_2, 0F, aY2, ModelZombieCraftech.NPI2);
        this.cote_3_2.renderWithRotation(scale);

        this.setRotation(this.cote_3_3, aX1, aY1, ModelZombieCraftech.NPI2);
        this.cote_3_3.renderWithRotation(scale);

        this.setRotation(this.cote_4_1, 0F, 0F, ModelZombieCraftech.NPI2);
        this.cote_4_1.renderWithRotation(scale);

        this.setRotation(this.cote_4_2, 0F, aY2, ModelZombieCraftech.NPI2);
        this.cote_4_2.renderWithRotation(scale);

        this.setRotation(this.cote_4_3, -1.456176E-07F, aY1, ModelZombieCraftech.NPI2);
        this.cote_4_3.renderWithRotation(scale);

        this.setRotation(this.cote_5_1, 0F, 0F, ModelZombieCraftech.NPI2);
        this.cote_5_1.renderWithRotation(scale);

        this.setRotation(this.cote_5_2, 0F, aY2, ModelZombieCraftech.NPI2);
        this.cote_5_2.renderWithRotation(scale);

        this.setRotation(this.cote_5_3, -6.134788E-08F, aY1, ModelZombieCraftech.NPI2);
        this.cote_5_3.renderWithRotation(scale);

        this.setRotation(this.cote_6_1, 0F, 0F, ModelZombieCraftech.NPI2);
        this.cote_6_1.renderWithRotation(scale);

        this.setRotation(this.cote_6_2, 0F, aY2, ModelZombieCraftech.NPI2);
        this.cote_6_2.renderWithRotation(scale);

        this.setRotation(this.cote_6_3, aX1, aY1, ModelZombieCraftech.NPI2);
        this.cote_6_3.renderWithRotation(scale);

        this.setRotation(this.column, 0F, 0F, ModelZombieCraftech.NPI2);
        this.column.renderWithRotation(scale);
    }

    @Override
    public void render(Entity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw,
            float headPitch, float scale)
    {
        final float headX = headPitch / (180F / (float) Math.PI);
        final float headY = netHeadYaw / (180F / ModelZombieCraftech.PI);
        final float headZ = 0F;

        final float armX = MathHelper.sin(limbSwingAmount * 0.067F) * 0.05F;
        final float armY = 0F;
        final float armZ = MathHelper.cos(limbSwingAmount * 0.09F) * 0.05F;

        final float legX = MathHelper.cos(limbSwing * 0.6662F + ModelZombieCraftech.PI) * 1.4F * limbSwingAmount;
        final float legY = 0F;
        final float legZ = 0F;

        final float bodyX = 0F;
        final float bodyY = 0F;
        final float bodyZ = 0F;

        this.renderHead(entity, headX, headY, headZ, scale);
        this.renderFullArm(entity, armX, armY, armZ + 0.05F, scale);
        this.renderBoneArm(entity, -armX, armY, -armZ + 0.05F, scale);
        this.renderFullLeg(entity, legX, legY, legZ, scale);
        this.renderBoneLeg(entity, -legX, legY, legZ, scale);
        this.renderBody(entity, bodyX, bodyY, bodyZ, scale);
    }

    private void setRotation(ModelRenderer model, float x, float y, float z)
    {
        model.rotateAngleX = x;
        model.rotateAngleY = y;
        model.rotateAngleZ = z;
    }

    private ModelRenderer createModel(int textureX, int textureY)
    {
        final ModelRenderer model = new ModelRenderer(this, textureX, textureY);
        model.setTextureSize(64, 64);
        return model;
    }

}
