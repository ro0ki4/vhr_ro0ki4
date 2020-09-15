package com.rookie.vhr.model;

/**
 * @author ro0ki4
 * @data 2020/9/13 21:25
 * version 1.0
 */
public class Meta {
    private Boolean keepalive;

    private Boolean requireauth;

    public Boolean getKeepalive() {
        return keepalive;
    }

    public void setKeepalive(Boolean keepalive) {
        this.keepalive = keepalive;
    }

    public Boolean getRequireauth() {
        return requireauth;
    }

    public void setRequireauth(Boolean requireauth) {
        this.requireauth = requireauth;
    }
}
