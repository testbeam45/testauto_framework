package com.dd.automation.utilities;

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.PropertiesConfigurationLayout;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PropUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    private File props;

    public PropUtils(File props) {
        this.props = props;
    }

    public String getValue(String property) {
        String propertyValue = null;
        PropertiesConfiguration config = new PropertiesConfiguration();
        PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();

        try (InputStream input = new FileInputStream(props)) {
            layout.load(config, new InputStreamReader(input, StandardCharsets.UTF_8));
            propertyValue = config.getString(property);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return propertyValue;
    }

    public List<String> getValues(String property) {
        List<String> values = new ArrayList<>();

        try {
            values = Arrays.asList(getValue(property).split(","));
            values.forEach(value -> value.trim());
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return values;
    }

    public void setValue(String property, String propertyValue) {
        PropertiesConfiguration config = new PropertiesConfiguration();
        PropertiesConfigurationLayout layout = new PropertiesConfigurationLayout();

        try (InputStream input = new FileInputStream(props)) {
            layout.load(config, new InputStreamReader(input, StandardCharsets.UTF_8));
            config.setProperty(property, propertyValue);
            layout.save(config, new FileWriter(props, false));
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }
}
