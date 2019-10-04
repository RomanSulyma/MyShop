package net.service;

import net.entity.Category;
import net.entity.Producers;
import net.entity.Products;
import net.form.SearchForm;

import java.util.List;

//contains methods for working with products
public interface ProductService {
    List<Products> listAllProducts(int page , int limit);

    List<Products> listProductsByCategory(String categoryURL ,int page , int limit);

    List<Category> listAllCategories();

    List<Producers> listAllProducers();

    int countAllProducts();

    int countProductsByCategory(String categoryUrl);

    List<Products> listProductsBySearchForm(SearchForm sf, int page , int limit);

    int countProductsBySearchForm(SearchForm sf);
}
