package net.model;

import net.Constants;
import net.entity.Products;

import javax.xml.bind.ValidationException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ShoppingCart implements Serializable {
    private static final long serialVersionUID = 1535770438453611801L;
    private Map<Integer, ShoppingCartItem> products = new LinkedHashMap<>();
    private int totalCount = 0;
    private BigDecimal totalCost = BigDecimal.ZERO;
    //checks the availability of the product in the basket and adds either as a new product or as an addition to the existing one
    public void addProduct(Products product, int count) throws ValidationException {
        validateShoppingCartSize(product.getId());
        ShoppingCartItem shoppingCartItem = products.get(product.getId());
        if (shoppingCartItem == null) {
            validateProductCount(count);
            shoppingCartItem = new ShoppingCartItem(product, count);
            products.put(product.getId(), shoppingCartItem);
        } else {
            validateProductCount(count + shoppingCartItem.getCount());
            shoppingCartItem.setCount(shoppingCartItem.getCount() + count);
        }
        refreshStatistics();
    }
    //remove product from basket
    public void removeProduct(Integer idProduct, int count) {
        ShoppingCartItem shoppingCartItem = products.get(idProduct);
        if (shoppingCartItem != null) {
            if (shoppingCartItem.getCount() > count) {
                shoppingCartItem.setCount(shoppingCartItem.getCount() - count);
            } else {
                products.remove(idProduct);
            }
            refreshStatistics();
        }
    }
    //gives the list of goods in the basket
    public Collection<ShoppingCartItem> getItems() {
        return products.values();
    }
    //gives the number of items in the basket
    public int getTotalCount() {
        return totalCount;
    }

    public BigDecimal getTotalCost() {
        return totalCost;
    }
    //checks the excess of the goods limit
    private void validateProductCount(int count) throws ValidationException {
        if(count > Constants.MAX_PRODUCT_COUNT_PER_SHOPPING_CART){
            throw new ValidationException("Limit for product count reached: count="+count);
        }
    }
    //checks if the cart limit is exceeded
    private void validateShoppingCartSize(int idProduct) throws ValidationException {
        if(products.size() > Constants.MAX_PRODUCTS_PER_SHOPPING_CART ||
                (products.size() == Constants.MAX_PRODUCTS_PER_SHOPPING_CART && !products.containsKey(idProduct))) {
            throw new ValidationException("Limit for ShoppingCart size reached: size="+products.size());
        }
    }
    //updates totalcount
    private void refreshStatistics() {
        totalCount = 0;
        totalCost = BigDecimal.ZERO;
        for (ShoppingCartItem shoppingCartItem : getItems()) {
            totalCount += shoppingCartItem.getCount();
            totalCost = totalCost.add(shoppingCartItem.getProduct().getPrice().multiply(BigDecimal.valueOf(shoppingCartItem.getCount())));
        }
    }

    @Override
    public String toString() {
        return String.format("ShoppingCart [products=%s, totalCount=%s ,totalCost=%s]", products, totalCount, totalCost);
    }

}