package net.kicchi.toptal.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import net.kicchi.toptal.config.ApplicationConfig;

/**
 * reads the properties file configuration.yml
 */
public class ConfigurationReaderUtil {

    public static ApplicationConfig getConfiguration(){
        try{
            File file = new File("src/test/resources/configuration.yml");
            ObjectMapper objectMapper = new ObjectMapper(new YAMLFactory());
            ApplicationConfig applicationConfig = objectMapper.readValue(file, ApplicationConfig.class);
            return applicationConfig;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

}