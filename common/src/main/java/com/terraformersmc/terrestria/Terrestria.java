package com.terraformersmc.terrestria;

import com.terraformersmc.terrestria.config.TerrestriaConfigManager;
import com.terraformersmc.terrestria.init.*;
import com.terraformersmc.terrestria.init.helpers.TerrestriaPlacementModifierType;
import com.terraformersmc.terrestria.item.TerrestriaItemGroups;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;

public class Terrestria implements ModInitializer {
	public static final String MOD_ID = "terrestria";
	public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

	private static final TerrestriaConfigManager CONFIG_MANAGER = new TerrestriaConfigManager();

	private static Boolean initialized = false;
	private static final ArrayList<Runnable> RUNNABLES = new ArrayList<>(1);

	private static void register() {
		// Load the general config if it hasn't been loaded already
		CONFIG_MANAGER.getGeneralConfig();

		TerrestriaBlocks.init();
		TerrestriaItems.init();
		TerrestriaPlacementModifierType.init();
		TerrestriaFoliagePlacerTypes.init();
		TerrestriaTrunkPlacerTypes.init();
		TerrestriaTreeDecorators.init();
		TerrestriaFeatures.init();
		TerrestriaStructures.init();
		TerrestriaTrades.register();
		TerrestriaVillagerTypes.init();
		TerrestriaItemGroups.init();
		TerrestriaRegistryAliases.init();
	}

	@Override
	public void onInitialize() {
		register();

		// This must be after TraverseBiomes.register()
		CONFIG_MANAGER.getBiomeConfig();

		if (!FabricLoader.getInstance().isModLoaded("terrestria-worldgen")) {
			Terrestria.LOGGER.info("No Terrestria worldgen module present; Terrestria biomes will not generate.");
		}

		// At this point Terrestria is completely initialized.
		initialized = true;
		for (Runnable callback : RUNNABLES) {
			callback.run();
		}
	}

	public static void callbackWhenInitialized(Runnable callback) {
		if (initialized) {
			callback.run();
		} else {
			RUNNABLES.add(callback);
		}
	}

	public static TerrestriaConfigManager getConfigManager() {
		return CONFIG_MANAGER;
	}
}
