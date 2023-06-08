package MagicWords.client.particle;

import MagicWords.misc.ModParticles;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class MagicParticleOptions implements ParticleOptions {
    public static final MagicParticleOptions.Deserializer DESERIALIZER = new MagicParticleOptions.Deserializer();
    public static final Codec<MagicParticleOptions> CODEC = RecordCodecBuilder.create(instance -> {
        return instance.group( Codec.DOUBLE.fieldOf("targetX").forGetter(MagicParticleOptions::getTargetPosX),
                                Codec.DOUBLE.fieldOf("targetY").forGetter(MagicParticleOptions::getTargetPosY),
                                Codec.DOUBLE.fieldOf("targetZ").forGetter(MagicParticleOptions::getTargetPosZ),
                                Codec.INT.fieldOf("packedARGB").forGetter(MagicParticleOptions::getPackedColorARGB)
                            ).apply(instance, MagicParticleOptions::new);
    });

    private final double targetPosX;
    private final double targetPosY;
    private final double targetPosZ;
    private final int packedColorARGB;

    public MagicParticleOptions( double targetX, double targetY, double targetZ, int packedARGB) {
        this.targetPosX = targetX;
        this.targetPosY = targetY;
        this.targetPosZ = targetZ;
        this.packedColorARGB = packedARGB;
    }

    public double getTargetPosX() {
        return targetPosX;
    }

    public double getTargetPosY() {
        return targetPosY;
    }

    public double getTargetPosZ() {
        return targetPosZ;
    }

    public int getPackedColorARGB() {
        return packedColorARGB;
    }

    @Override
    public @NotNull ParticleType<MagicParticleOptions> getType() {
        return ModParticles.MAGIC_PARTICLE_TYPE.get();
    }

    @Override
    public void writeToNetwork(@NotNull FriendlyByteBuf pBuffer) {
        pBuffer.writeDouble(targetPosX);
        pBuffer.writeDouble(targetPosY);
        pBuffer.writeDouble(targetPosZ);
        pBuffer.writeInt(packedColorARGB);
    }

    @Override
    public @NotNull String writeToString() {
        return String.format(Locale.ROOT
                , "%s %.2f %.2f %.2f %d"
                , ForgeRegistries.PARTICLE_TYPES.getKey(this.getType())
                , targetPosX, targetPosY, targetPosZ, packedColorARGB);
    }


    public static class Deserializer implements ParticleOptions.Deserializer<MagicParticleOptions> {

        @Override
        public @NotNull MagicParticleOptions fromCommand(@NotNull ParticleType<MagicParticleOptions> pParticleType, @NotNull StringReader pReader) throws CommandSyntaxException {
            pReader.expect(' ');
            var x = pReader.readDouble();
            pReader.expect(' ');
            var y = pReader.readDouble();
            pReader.expect(' ');
            var z = pReader.readDouble();
            pReader.expect(' ');
            var colourPacked = pReader.readInt();
            return new MagicParticleOptions(x, y, z, colourPacked);
        }

        @Override
        public @NotNull MagicParticleOptions fromNetwork(@NotNull ParticleType<MagicParticleOptions> pParticleType, @NotNull FriendlyByteBuf pBuffer) {
            var x = pBuffer.readDouble();
            var y = pBuffer.readDouble();
            var z = pBuffer.readDouble();
            var packedColour = pBuffer.readInt();
            return new MagicParticleOptions(x, y, z, packedColour);
        }
    }

    public static class Type extends ParticleType<MagicParticleOptions> {
        public Type() {
            super(false, MagicParticleOptions.DESERIALIZER);
        }

        @Override
        public @NotNull Codec<MagicParticleOptions> codec() {
            return CODEC;
        }
    }
}
