package archives.tater.bloodmending;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;
import net.minecraft.world.level.gamerules.GameRules;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static net.minecraft.util.Mth.clamp;
import static net.minecraft.util.Mth.lerp;

public class BloodMending implements ModInitializer {
	public static final String MOD_ID = "bloodmending";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final GameRule<Double> MAX_EXPERIENCE_HEALING_FACTOR = GameRuleBuilder.forDouble(1.0)
			.minValue(0.0)
			.buildAndRegister(id("max_experience_healing_factor"));

	public static final GameRule<Double> MIN_EXPERIENCE_HEALING_FACTOR = GameRuleBuilder.forDouble(0.25)
			.minValue(0.0)
			.buildAndRegister(id("min_experience_healing_factor"));

	public static final GameRule<Double> EXPERIENCE_OVERKILL_BONUS_FACTOR = GameRuleBuilder.forDouble(0.5)
			.minValue(0.0)
			.buildAndRegister(id("experience_overkill_bonus_factor"));

	public static final int MAX_ARMOR = 20;

	public static float getExperienceHealingFactor(int armor, GameRules gameRules) {
		return lerp(
				clamp((float) armor / MAX_ARMOR, 0, 1),
				gameRules.get(MAX_EXPERIENCE_HEALING_FACTOR).floatValue(),
				gameRules.get(MIN_EXPERIENCE_HEALING_FACTOR).floatValue()
		);
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
	}
}