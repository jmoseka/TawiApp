package com.mila.agrimo;

public class ProductModel {

    String userId, productRandom, productCategory, image, image1, image2,
            image3, productTitle, productDescription, productLocation, productCurrency,
            productTime, country, additionalInfo, section;



    public ProductModel() {

    }


    public ProductModel(String userId, String productRandom, String productCategory, String image, String image1,
                        String image2, String image3, String productTitle, String productDescription,
                        String productLocation, String productCurrency,
                        String productTime, String country, String additionalInfo, String section) {
        this.userId = userId;
        this.productRandom = productRandom;
        this.productCategory = productCategory;
        this.image = image;
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.productTitle = productTitle;
        this.productDescription = productDescription;
        this.productLocation = productLocation;
        this.productCurrency = productCurrency;
        this.productTime = productTime;
        this.country = country;
        this.additionalInfo = additionalInfo;
        this.section = section;

    }

    public ProductModel(String image, String productTitle, String productLocation, String productCurrency, String productTime, String productRandom) {
        this.image = image;
        this.productTitle = productTitle;
        this.productLocation = productLocation;
        this.productCurrency = productCurrency;
        this.productTime = productTime;
        this.productRandom = productRandom;

    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
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





