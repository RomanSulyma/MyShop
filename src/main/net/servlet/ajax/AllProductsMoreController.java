package net.servlet.ajax;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.Constants;
import net.entity.Products;
import net.servlet.AbstractController;
import net.util.RoutingUtils;

//handles loadmore and loads products for all products
@WebServlet("/ajax/html/more/products")
public class AllProductsMoreController extends AbstractController {
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Products> products = getProductService().listAllProducts(getPage(req), Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("products",products);
        RoutingUtils.forwardToFragment("product-list.jsp", req, resp);
    }
}
