package com.example.fastuse.mixin;

import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.TntMinecartEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * Идея как в Marlow's Crystal Optimizer: пакет удара уходит на сервер как обычно,
 * а на твоём экране вагонетка с TNT исчезает сразу, не дожидаясь ответа сервера.
 * Если сервер удар не засчитал, вагонетка просто вернётся при следующем обновлении.
 */
@Mixin(ClientPlayerInteractionManager.class)
public abstract class InteractionManagerMixin {

    @Inject(method = "attackEntity", at = @At("TAIL"))
    private void fastuse$predictTntMinecartBreak(PlayerEntity player, Entity target, CallbackInfo ci) {
        if (!(target instanceof TntMinecartEntity cart)) return;
        if (!player.getWorld().isClient()) return;

        // Прогнозируем уничтожение только если удар точно смертельный
        boolean lethal;
        if (player.isCreative()) {
            lethal = true;
        } else {
            // Удар на полную силу (кулдаун почти восстановлен) и урон достаточен для поломки
            float charge = player.getAttackCooldownProgress(0.5f);
            double damage = player.getAttributeValue(EntityAttributes.ATTACK_DAMAGE);
            AbstractMinecartEntity base = cart;
            lethal = charge >= 0.9f && (base.getDamageWobbleStrength() + (float) damage * 10.0f) > 40.0f;
        }

        if (lethal) {
            cart.discard();
        }
    }
}
