package net.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

import net.form.ProductForm;
import net.form.SearchForm;
import net.service.OrderService;
import net.service.ProductService;
import net.service.SocialService;
import net.service.impl.ServiceManager;


public abstract class AbstractController extends HttpServlet {
    private static final long serialVersionUID = -2031074947573473708L;

    private ProductService productService;
    private OrderService orderService;
    private SocialService socialService;
    //initializes services, receiving singleton data objects of 2 classes
    @Override
    public final void init() throws ServletException {
        productService = ServiceManager.getInstance(getServletContext()).getProductService();
        orderService = ServiceManager.getInstance(getServletContext()).getOrderService();
        socialService = ServiceManager.getInstance(getServletContext()).getSocialService();
    }
    //service data getters, cannot be overrided


    public SocialService getSocialService() {
        return socialService;
    }

    public final ProductService getProductService() {
        return productService;
    }

    public final OrderService getOrderService() {
        return orderService;
    }
    //returns the number of pages to display
    public final int getPageCount(int totalCount, int itemsPerPage)
    {
        int res = totalCount / itemsPerPage;
        if(res * itemsPerPage != totalCount){
            res++;
        }
        return res;
    }
    //returns the parameter of the current page to display that comes from JS
    public final int getPage(HttpServletRequest request)
    {
        try {
            return Integer.parseInt(request.getParameter("page"));
        }catch (Exception e) {
            return 1;
        }
    }
    //creates a search form with parameters from the request
    public final SearchForm createSearchForm(HttpServletRequest req)
    {
        return  new SearchForm(req.getParameter("query"),req.getParameterValues("category"),req.getParameterValues("producer") , req.getParameterValues("price_value"));
    }
    //creates a form
    public final ProductForm createProductForm(HttpServletRequest req)
    {
        return  new ProductForm(Integer.parseInt(req.getParameter("idProduct")) , Integer.parseInt(req.getParameter("count")));
    }
}
