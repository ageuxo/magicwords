package MagicWords.client.particle;

import MagicWords.misc.Utils;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.ThreadLocalRandom;

public class MagicParticle extends TextureSheetParticle {
    private final double targetX, targetY, targetZ;
    private final float fadeMult;

    public MagicParticle(ClientLevel pLevel, double pX, double pY, double pZ, int packedARGB, double targetX, double targetY, double targetZ) {
        super(pLevel, pX, pY, pZ);
        this.targetX = targetX;
        this.targetY = targetY;
        this.targetZ = targetZ;
        this.setAlpha(Utils.unpackAlphaToFloat(packedARGB));
        this.setColor(Utils.unpackRedToFloat(packedARGB), Utils.unpackGreenToFloat(packedARGB), Utils.unpackBlueToFloat(packedARGB));
        this.lifetime = ThreadLocalRandom.current().nextInt(10, 60);
        this.xd = (Math.random() * 2.0D - 1.0D) * (double)0.4F;
        this.yd = (Math.random() * 2.0D - 1.0D) * (double)0.4F;
        this.zd = (Math.random() * 2.0D - 1.0D) * (double)0.4F;
        this.fadeMult = ThreadLocalRandom.current().nextFloat(0.9f, 0.99f);
    }

    @Override
    public void tick() {
        double moveX, moveY, moveZ;
        moveX = -(x - targetX) * 0.05d;
        moveY = -(y - targetY) * 0.05d;
        moveZ = -(z - targetZ) * 0.05d;

        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.move(moveX*xd, moveY*yd, moveZ*zd);
            this.alpha = this.alpha*fadeMult;
        }
    }

    @Override
    public void move(double pX, double pY, double pZ) {
        this.setBoundingBox(this.getBoundingBox().move(pX, pY, pZ));
        this.setLocationFromBoundingbox();
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }


    public static class Provider implements ParticleProvider<MagicParticleOptions> {
        private final SpriteSet spriteSet;

        public Provider(SpriteSet sprites) {
            this.spriteSet = sprites;
        }

        @Nullable
        @Override
        public Particle createParticle(@NotNull MagicParticleOptions pType, @NotNull ClientLevel pLevel, double pX, double pY, double pZ, double pXSpeed, double pYSpeed, double pZSpeed) {
            MagicParticle particle = new MagicParticle(pLevel, pX, pY, pZ, pType.getPackedColorARGB(), pType.getTargetPosX(), pType.getTargetPosY(), pType.getTargetPosZ());
            particle.pickSprite(this.spriteSet);
            particle.setParticleSpeed(pXSpeed, pYSpeed, pZSpeed);
            return particle;
        }
    }

}
