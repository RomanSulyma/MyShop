package net.model;

import net.entity.Products;

import java.io.Serializable;

//describes one item from the basket
public class ShoppingCartItem implements Serializable {
    private static final long serialVersionUID = 6436798264138502851L;
    private Products product;
    private int count;
    public ShoppingCartItem() {
        super();
    }
    public ShoppingCartItem(Products product, int count) {
        super();
        this.product = product;
        this.count = count;
    }

    public Products getProduct() {
        return product;
    }

    public void setProduct(Products product) {
        this.product = product;
    }

    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ShoppingCartItem{" +
                "product=" + product +
                ", count=" + count +
                '}';
    }
}
