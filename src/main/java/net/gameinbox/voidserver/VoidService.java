package net.gameinbox.voidserver;

import net.gameinbox.voidserver.config.ConfigurationManager;
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileReader;
import java.io.IOException;

public class VoidService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoidService.class);

    /**
     * Program launch function
     * @param args program launch arguments
     */
    public static void main(String[] args) throws IOException, XmlPullParserException {
        final VoidService main = new VoidService(args);
    }

    /**
     * Class constructor
     * @param args program launch arguments
     */
    public VoidService(String[] args) throws IOException, XmlPullParserException {
        // Version warn message
        LOGGER.info("-----------------------------------------");
        LOGGER.info("Launching Void Limbo server...");

        final MavenXpp3Reader reader = new MavenXpp3Reader();
        final Model model = reader.read(new FileReader("pom.xml"));

        final String version = model.getVersion();
        final boolean isAlphaBuild = version.contains("ALPHA");
        final boolean isBetaBuild = version.contains("BETA");

        if(isAlphaBuild) {
            LOGGER.warn("WATCH OUT! You are currently using the alpha build " + version + "!");
            LOGGER.warn("A bug or a crash can occur at any time!");
        }
        else if(isBetaBuild) {
            LOGGER.warn("WARNING! You are currently using the beta build " + version + "!");
            LOGGER.warn("A bug or a crash can occur at any time!");
        }
        else
            LOGGER.info("Current version: " + version);

        LOGGER.info("-----------------------------------------");

        // Configuration loading
        LOGGER.info("Loading configuration...");
        final ConfigurationManager configurationManager = new ConfigurationManager();
        if(!ConfigurationManager.CONFIGURATION_FILE.exists()) {
            configurationManager.generateDefaultConfigurationFile();
            LOGGER.info("Configuration file not found, created one by default.");
        }
        configurationManager.loadConfiguration();
    }

}
