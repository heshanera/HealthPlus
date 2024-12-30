package com.hms.hms_test_2;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class SystemConfiguration {

    private static SystemConfiguration instance;
    private Properties properties;
    private final String configFilePath = "config.properties";

    private SystemConfiguration() {
        properties = new Properties();
        loadConfig();
    }

    public static synchronized SystemConfiguration getInstance() {
        if (instance == null) {
            instance = new SystemConfiguration();
        }
        return instance;
    }

    private void loadConfig() {
        try (FileInputStream fis = new FileInputStream(configFilePath)) {
            properties.load(fis);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to load configuration file.");
        }
    }

    public synchronized String getConfig(String key) {
        return properties.getProperty(key);
    }

    public synchronized void updateConfig(String key, String value) {
        properties.setProperty(key, value);
    }

    public synchronized void saveConfig() {
        try (FileOutputStream fos = new FileOutputStream(configFilePath)) {
            properties.store(fos, "Updated configuration");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save configuration file.");
        }
    }

    public synchronized void reloadConfig() {
        loadConfig();
    }
}