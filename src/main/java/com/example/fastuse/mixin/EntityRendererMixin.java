package com.example.fastuse.mixin;

import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * Оптимизация FPS: не рисуем далёкие вагонетки с TNT и стрелы.
 * Сама игровая логика и сервер не затрагиваются, меняется только отрисовка.
 */
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    // Дальность отрисовки в блоках (квадрат считаем один раз)
    private static final double FASTUSE_MAX_DIST = 48.0;
    private static final double FASTUSE_MAX_DIST_SQ = FASTUSE_MAX_DIST * FASTUSE_MAX_DIST;

    @Inject(method = "shouldRender", at = @At("HEAD"), cancellable = true)
    private void fastuse$cullFar(T entity, Frustum frustum, double camX, double camY, double camZ,
                                 CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof TntMinecartEntity || entity instanceof PersistentProjectileEntity) {
            if (entity.squaredDistanceTo(camX, camY, camZ) > FASTUSE_MAX_DIST_SQ) {
                cir.setReturnValue(false);
            }
        }
    }
}
