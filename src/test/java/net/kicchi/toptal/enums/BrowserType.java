package net.kicchi.toptal.enums;

import java.util.Arrays;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BrowserType {
    CHROME(1, "chrome"),
    CHROME_HEADLESS(2, "chrome-headless"),
    CHROME_REMOTE(3, "chrome-remote"),
    FIREFOX(4, "firefox"),
    FIREFOX_HEADLESS(5, "firefox-headless"),
    INTERNET_EXPLORER(6, "internet-explorer"),
    EDGE(7, "edge"),
    SAFARI(8, "safari");

    private int typeId;
    private String name;

    public String getName(){return name;}

    public static BrowserType getById(int id){
        return Arrays.stream(values()).filter(b -> b.typeId == id).findFirst().get();
    }

    public static BrowserType getByName(String browserName){
        return Arrays.stream(values()).filter(b -> b.name.equals(browserName)).findFirst().get();
    }
}
