package net.form;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//stores data from a search form
public class SearchForm {

    private String query;
    private List<Integer> categories;
    private List<Integer> producers;
    private List<Integer> price;


    public SearchForm(String query, String[] categories, String[] producers , String[] price) {
        super();
        this.query = query;
        this.categories = convert(categories);
        this.producers = convert(producers);
        this.price = convert(price);
    }
    //splits an array of parameters from the search form into a list of integers
    private  List<Integer> convert(String[] args) {
        if (args == null) {
            return Collections.emptyList();
        } else {
            List<Integer> res = new ArrayList<>(args.length);
            for (String arg : args) {
                res.add(Integer.parseInt(arg));
            }
            return res;
        }
    }


    public String getQuery(){
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<Integer> getCategories() {
        return categories;
    }

    public void setCategories(List<Integer> categories) {
        this.categories = categories;
    }

    public List<Integer> getProducers() {
        return producers;
    }

    public void setProducers(List<Integer> producers) {
        this.producers = producers;
    }

    public List<Integer> getPrice() {
        return price;
    }

    public void setPrice(List<Integer> price) {
        this.price = price;
    }
    //3 methods checks for empty parameters of categories, manufacturers, prices
    public boolean isCategoriesNotEmpty(){
        return  !categories.isEmpty();
    }

    public boolean isProducersNotEmpty(){
        return  !producers.isEmpty();
    }

    public boolean isPriceNotEmpty(){
        System.out.println(price.contains(3000));
        return  !price.isEmpty();
    }
}
