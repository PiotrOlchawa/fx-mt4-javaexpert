package org.mt4expert.javaexpert.datareader;

import org.mt4expert.javaexpert.config.ExpertConfigurator;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static org.mt4expert.javaexpert.config.ExpertConfigurator.CONFIG_FILENAME;

public class FileConfigReader {

    private String csvFullPathFile = null;
    private static BufferedReader br = null;
    private static String line = "";
    private static final String configParametersSplitBy = "=";


    public static Map<String, String> readConfig() {

        Map<String, String> configParameters = new HashMap<>();
        try {
            //br = new BufferedReader(new FileReader(ExpertConfigurator.CONFIG_FILENAME));
            br = new BufferedReader(new InputStreamReader(CONFIG_FILENAME.openStream()));
            while ((line = br.readLine()) != null) {
                String[] configParam = line.split(configParametersSplitBy);
                if(configParam.length==2) {
                    configParameters.put(configParam[0], configParam[1]);
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return configParameters;
    }
}