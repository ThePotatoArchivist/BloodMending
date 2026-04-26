package archives.tater.bloodmending.mixin;

import archives.tater.bloodmending.BloodMending;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> type, Level level) {
        super(type, level);
    }

    @Unique
    private float overkill = 0;

    @Inject(
            method = "setHealth",
            at = @At("HEAD")
    )
    private void saveOverkill(float health, CallbackInfo ci) {
        if (health < 0) overkill = -health;
    }

    @ModifyExpressionValue(
            method = "getExperienceReward",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/LivingEntity;getBaseExperienceReward(Lnet/minecraft/server/level/ServerLevel;)I")
    )
    private int overkillBonus(int original) {
        if (!(level() instanceof ServerLevel serverLevel)) return original;
        return original + (int) (serverLevel.getGameRules().get(BloodMending.EXPERIENCE_OVERKILL_BONUS_FACTOR) * overkill);
    }
}
