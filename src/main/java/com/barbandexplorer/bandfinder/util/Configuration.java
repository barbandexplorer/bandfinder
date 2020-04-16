package com.barbandexplorer.bandfinder.util;

import org.yaml.snakeyaml.Yaml;

import java.io.InputStreamReader;
import java.util.Map;

public class Configuration {

    private static Configuration instance;

    public static Configuration getInstance() {

        if(instance == null){
            instance = new Configuration();
        }

        return instance;
    }

    private Map<String,Object> fieldToObj;

    public Configuration(){
        Yaml yaml = new Yaml();
        fieldToObj = yaml.load(new InputStreamReader(ClassLoader.getSystemResourceAsStream("application.yml")));
    }

    public Object getProperty(String... names){

        Object next = fieldToObj;

        for (String name : names) {
            next = ((Map<String,Object>)next).get(name);
        }

        return next;
    }
}
