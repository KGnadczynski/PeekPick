package com.tackpad.configs;

import de.bytefish.fcmjava.http.options.IFcmClientSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FcmSettings implements IFcmClientSettings {

    @Value("${fcm.api-key}")
    private String apiKey;

    @Value("${fcm.url}")
    private String url;

    @Override
    public String getFcmUrl() {
        return  this.url;
    }

    @Override
    public String getApiKey() {
        return this.apiKey;
    }

}
