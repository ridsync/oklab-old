package com.okitoki.checklist.network.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root
public class ProductItem {

    @Element(name = "title")
    String title;

    @Element(name = "link")
    String link;

    @Element(name = "image")
    String image;

    @Element(name = "lprice")
    int lprice;

    @Element(name = "hprice")
    int hprice;

    @Element(name = "mallName")
    String mallName;

    @Element(name = "productId")
    long productId;

    @Element(name = "productType")
    int productType;

    public int getHprice() {
        return hprice;
    }

    public void setHprice(int hprice) {
        this.hprice = hprice;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getLprice() {
        return lprice;
    }

    public void setLprice(int lprice) {
        this.lprice = lprice;
    }

    public String getMallName() {
        return mallName;
    }

    public void setMallName(String mallName) {
        this.mallName = mallName;
    }

    public long getProductId() {
        return productId;
    }

    public void setProductId(long productId) {
        this.productId = productId;
    }

    public int getProductType() {
        return productType;
    }

    public void setProductType(int productType) {
        this.productType = productType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}