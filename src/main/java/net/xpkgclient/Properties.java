package net.xpkgclient;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

import java.io.IOException;

/**
 * Values of properties stored in resources/project.properties.
 */
@UtilityClass
public class Properties {

    private final java.util.Properties properties = new java.util.Properties();

    @Getter private String version;
    @Getter private String artifactId;

    /**
     * Initialize all properties.
     */
    @SneakyThrows(IOException.class)
    public void init() {
        properties.load(Properties.class.getResourceAsStream("/project.properties"));
        version = properties.getProperty("version", "UNKNOWN");
        artifactId = properties.getProperty("artifactId", "UNKNOWN");
    }
}
