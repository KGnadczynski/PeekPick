package com.tackpad.configs;

import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "fcm")
@Component
public class FcmSettings implements IFcmClientSettings {
    private String apiKey;
    private String url;

    @Override
    public String getFcmUrl() {
        return  this.url;
    }

    @Override
    public String getApiKey() {
        return this.apiKey;
    }

    //get / set methods
}
