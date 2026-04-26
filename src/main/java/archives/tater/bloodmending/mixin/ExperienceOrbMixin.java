package archives.tater.bloodmending.mixin;

import archives.tater.bloodmending.BloodMending;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.phys.Vec3;

import static java.lang.Math.min;
import static net.minecraft.util.Mth.floor;

@Mixin(ExperienceOrb.class)
public class ExperienceOrbMixin {
	@WrapOperation(
			method = "playerTouch",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;repairPlayerItems(Lnet/minecraft/server/level/ServerPlayer;I)I")
	)
	private int healPlayer(ExperienceOrb instance, ServerPlayer player, int amount, Operation<Integer> original) {
		var healthMissing = player.getMaxHealth() - player.getHealth();
		if (healthMissing < 1) return amount;
		double xpHealingFactor = BloodMending.getExperienceHealingFactor(player.getArmorValue(), player.level().getGameRules());
		var amountHealed = min(healthMissing, amount * (float) xpHealingFactor);
		player.heal(amountHealed);
		return floor(amount - amountHealed / xpHealingFactor);
	}

	@WrapOperation(
			method = "awardWithDirection",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;getExperienceValue(I)I")
	)
	private static int limitSize(int maxValue, Operation<Integer> original, ServerLevel level) {
		return level.getGameRules().get(BloodMending.MESSY_XP) ? min(3, maxValue) : original.call(maxValue);
	}

	@WrapOperation(
			method = "awardWithDirection",
			at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/ExperienceOrb;tryMergeToExisting(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/phys/Vec3;I)Z")
	)
	private static boolean noMerge(ServerLevel level, Vec3 pos, int value, Operation<Boolean> original) {
		return !level.getGameRules().get(BloodMending.MESSY_XP) && original.call(level, pos, value);
	}
}