package net.gameinbox.voidserver.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * ConfigurationManager class is used for loading and retrieving configuration for the limbo server
 */
public class ConfigurationManager {

    public static final File CONFIGURATION_FILE = new File("limbo.json");

    /**
     * Current loaded configuration, null if not loaded
     */
    private VoidConfiguration loadedConfiguration;

    /**
     * Gson used to open the configuration file
     */
    private final Gson gson;

    public ConfigurationManager() {
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    /**
     * Load configuration file
     * @throws IOException If file is not found
     */
    public void loadConfiguration() throws IOException {
        final FileReader fileReader = new FileReader(ConfigurationManager.CONFIGURATION_FILE);
        final JsonReader jsonReader = new JsonReader(fileReader);

        this.loadedConfiguration = gson.fromJson(jsonReader, VoidConfiguration.class);

        jsonReader.close();
        fileReader.close();
    }

    /**
     * Generate configuration file with default configuration
     * @throws IOException If file writing is not complete
     */
    public void generateDefaultConfigurationFile() throws IOException {
        final FileWriter fileWriter = new FileWriter(ConfigurationManager.CONFIGURATION_FILE);

        gson.toJson(new VoidConfiguration(), fileWriter);

        fileWriter.flush();
        fileWriter.close();
    }

}
