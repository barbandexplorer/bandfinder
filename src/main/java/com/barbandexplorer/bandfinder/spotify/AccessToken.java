package com.barbandexplorer.bandfinder.spotify;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.time.DateUtils;

import java.util.Date;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccessToken {

    @JsonProperty("access_token")
    private String accessToken;
    @JsonProperty("expires_in")
    private int expiresIn;
    private Date expiresAt;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
        expiresAt = DateUtils.addSeconds(new Date(),expiresIn);
    }

    public boolean hasExpired(){
        return expiresAt.after(new Date());
    }
}
