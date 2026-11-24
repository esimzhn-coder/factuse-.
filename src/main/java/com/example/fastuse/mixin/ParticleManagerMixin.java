package com.example.fastuse.mixin;

import com.example.fastuse.ArrowTickFlag;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/** FPS: убираем «хвост» критических частиц у далёких стрел (лук и арбалет). */
@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    private static final double FASTUSE_PARTICLE_DIST_SQ = 24.0 * 24.0;

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("HEAD"), cancellable = true)
    private void fastuse$skipFarArrowTrail(ParticleEffect effect, double x, double y, double z,
                                           double vx, double vy, double vz,
                                           CallbackInfoReturnable<Particle> cir) {
        if (!ArrowTickFlag.inArrowTick || effect.getType() != ParticleTypes.CRIT) return;
        var player = MinecraftClient.getInstance().player;
        if (player != null && player.squaredDistanceTo(x, y, z) > FASTUSE_PARTICLE_DIST_SQ) {
            cir.setReturnValue(null);
        }
    }
}
