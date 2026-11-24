package com.example.fastuse.mixin;

import com.example.fastuse.ArrowTickFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PersistentProjectileEntity.class)
public abstract class PersistentProjectileMixin {
    @Inject(method = "tick", at = @At("HEAD"))
    private void fastuse$tickStart(CallbackInfo ci) {
        ArrowTickFlag.inArrowTick = ((Entity) (Object) this).getEntityWorld().isClient();
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private void fastuse$tickEnd(CallbackInfo ci) {
        ArrowTickFlag.inArrowTick = false;
    }
}
