package archives.tater.bloodmending;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.gamerule.v1.GameRuleBuilder;

import net.minecraft.resources.Identifier;
import net.minecraft.world.level.gamerules.GameRule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BloodMending implements ModInitializer {
	public static final String MOD_ID = "bloodmending";

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final GameRule<Double> EXPERIENCE_HEALING_FACTOR = GameRuleBuilder.forDouble(0.5)
			.minValue(0.0)
			.buildAndRegister(id("experience_healing_factor"));

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
	}
}