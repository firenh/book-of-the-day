package fireopal.bookoftheday;

import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BookOfTheDay implements ModInitializer {
    public static final FOModVersion VERSION = FOModVersion.fromString("1.2.0");
    public static final String MODID = "book_of_the_day";
    public static final Logger LOGGER = LogManager.getLogger(MODID);

    public static Config CONFIG;


    public static void log(String output) {
        LOGGER.info(output);
    }
    
    public void onInitialize() {
        loadConfigFromFile();
    }

    public static void loadConfigFromFile() {
        CONFIG = Config.init();
        if (CONFIG.log_when_loaded) log("Loaded config for " + MODID);
    }
}
