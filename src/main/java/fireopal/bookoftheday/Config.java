package fireopal.bookoftheday;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;

import com.google.gson.GsonBuilder;

public class Config {
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    //Config Default Values

    public String comment = "Chances are a decimal on a scale of 0 to 1";
    public String CONFIG_VERSION_DO_NOT_TOUCH_PLS = BookOfTheDay.VERSION.toString();

    public double chance_to_reset_when_traded_with = 1.0;
    public double chance_to_reset_when_not_traded_with = 1.0 / 3.0;
    public double chance_to_randomize_price = 0.5;
    public int min_price_difference_if_prices_randomized = -5;
    public int max_price_difference_if_prices_randomized = 5;
    public boolean log_when_loaded = false;
    
    //~~~~~~~~

    public static Config init() {
        Config config = null;

        try {
            Path configPath = Paths.get("", "config", "bookoftheday.json");

            if (Files.exists(configPath)) {
                config = gson.fromJson(
                    new FileReader(configPath.toFile()),
                    Config.class
                );

                if (!config.CONFIG_VERSION_DO_NOT_TOUCH_PLS.equals(BookOfTheDay.VERSION.toString())) {
                    config.CONFIG_VERSION_DO_NOT_TOUCH_PLS = BookOfTheDay.VERSION.toString();

                    BufferedWriter writer = new BufferedWriter(
                        new FileWriter(configPath.toFile())
                    );

                    writer.write(gson.toJson(config));
                    writer.close();
                }

            } else {
                config = new Config();
                Paths.get("", "config").toFile().mkdirs();

                BufferedWriter writer = new BufferedWriter(
                    new FileWriter(configPath.toFile())
                );

                writer.write(gson.toJson(config));
                writer.close();
            }


        } catch (IOException exception) {
            exception.printStackTrace();
        }

        return config;
    }
}
