package com.mila.agrimo;

public class ProductInfoModel {
    String productInfoName, productInfoPrice, productInfoDesc, productInfoLocation;

    public ProductInfoModel(String productInfoName, String productInfoPrice, String productInfoDesc, String productInfoLocation) {
        this.productInfoName = productInfoName;
        this.productInfoPrice = productInfoPrice;
        this.productInfoDesc = productInfoDesc;
        this.productInfoLocation = productInfoLocation;
    }

    public String getProductInfoName() {
        return productInfoName;
    }

    public void setProductInfoName(String productInfoName) {
        this.productInfoName = productInfoName;
    }

    public String getProductInfoPrice() {
        return productInfoPrice;
    }

    public void setProductInfoPrice(String productInfoPrice) {
        this.productInfoPrice = productInfoPrice;
    }

    public String getProductInfoDesc() {
        return productInfoDesc;
    }

    public void setProductInfoDesc(String productInfoDesc) {
        this.productInfoDesc = productInfoDesc;
    }

    public String getProductInfoLocation() {
        return productInfoLocation;
    }

    public void setProductInfoLocation(String productInfoLocation) {
        this.productInfoLocation = productInfoLocation;
    }
}
