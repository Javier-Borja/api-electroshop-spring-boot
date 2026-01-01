package com.example.electro_shop.modules.orders.dtos;

public class StripeResponse {

    private String url;

    public StripeResponse() {
    }

    public StripeResponse(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
