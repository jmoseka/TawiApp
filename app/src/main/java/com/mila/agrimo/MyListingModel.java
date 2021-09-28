package com.mila.agrimo;


public class MyListingModel {
    private String userId, productRandom, productCategory, image, productTitle, productDescription, productLocation, productCurrency, productTime;
   private Long timeStamp;

    public MyListingModel() {

    }


    public MyListingModel(String image, String productTitle, String productLocation, String productCurrency, String productTime, String productRandom) {
        this.image = image;
        this.productTitle = productTitle;
        this.productLocation = productLocation;
        this.productCurrency = productCurrency;
        this.productTime = productTime;
        this.productRandom = productRandom;

    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductRandom() {
        return productRandom;
    }

    public void setProductRandom(String productRandom) {
        this.productRandom = productRandom;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
    

    public String getProductTitle() {
        return productTitle;
    }

    public void setProductTitle(String productTitle) {
        this.productTitle = productTitle;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductLocation() {
        return productLocation;
    }

    public void setProductLocation(String productLocation) {
        this.productLocation = productLocation;
    }

    public String getProductCurrency() {
        return productCurrency;
    }

    public void setProductCurrency(String productCurrency) {
        this.productCurrency = productCurrency;
    }

    public String getProductTime() {
        return productTime;
    }

    public void setProductTime(String productTime) {
        this.productTime = productTime;
    }

}
