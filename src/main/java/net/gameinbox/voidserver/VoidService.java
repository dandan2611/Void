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

    private static final Logger logger = LoggerFactory.getLogger(VoidService.class);

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
        logger.info("-----------------------------------------");
        logger.info("Launching Void Limbo server...");

        final MavenXpp3Reader reader = new MavenXpp3Reader();
        final Model model = reader.read(new FileReader("pom.xml"));

        final String version = model.getVersion();
        final boolean isAlphaBuild = version.contains("ALPHA");
        final boolean isBetaBuild = version.contains("BETA");

        if(isAlphaBuild) {
            logger.warn("WATCH OUT! You are currently using the alpha build " + version + "!");
            logger.warn("A bug or a crash can occur at any time!");
        }
        else if(isBetaBuild) {
            logger.warn("WARNING! You are currently using the beta build " + version + "!");
            logger.warn("A bug or a crash can occur at any time!");
        }
        else
            logger.info("Current version: " + version);

        logger.info("-----------------------------------------");

        // Configuration loading
        logger.info("Loading configuration...");
        final ConfigurationManager configurationManager = new ConfigurationManager();
        if(!ConfigurationManager.CONFIGURATION_FILE.exists()) {
            configurationManager.generateDefaultConfigurationFile();
            logger.info("Configuration file not found, created one by default.");
        }
        configurationManager.loadConfiguration();
    }

}
