package com.tackpad.requests;

import javax.validation.constraints.NotNull;

/**
 * Created by Wojtek on 2017-03-05.
 */
public class Diggits {
    @NotNull
    public String url;

    @NotNull
    public String credentials;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCredentials() {
        return credentials;
    }

    public void setCredentials(String credentials) {
        this.credentials = credentials;
    }
}
