package net.kicchi.toptal.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.kicchi.toptal.enums.BrowserType;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ApplicationConfig {

    @JsonProperty("main-page-url")
    private String mainPageUrl;

    @JsonProperty("url-to-navigate")
    private String urlToNavigate;

    @JsonProperty("browser-type-id")
    private int browserTypeId;

    @JsonProperty("remote-grid-url")
    private String remoteGridUrl;

    @JsonProperty("api-base-url")
    private String apiBaseURL;

    public BrowserType getBrowserType(){
        return BrowserType.getById(browserTypeId);
    }


}
