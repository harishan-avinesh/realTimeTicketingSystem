package utility;

import java.io.IOException;
import java.util.logging.*;

public class LoggerConfig {
    // Configuring the logger for the entire application
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());

        try {
            // FileHandler for logging to a file
            FileHandler fileHandler = new FileHandler(clazz.getSimpleName() + ".log", true);
            fileHandler.setLevel(Level.ALL);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);


            // Set logger level
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to initialize logger: " + e.getMessage());
        }

        return logger;
    }
}
