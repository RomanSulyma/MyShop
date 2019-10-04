package net.servlet.ajax;

import net.Constants;
import net.entity.Products;
import net.servlet.AbstractController;
import net.util.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//ajax product request controller for products by category
@WebServlet("/ajax/html/more/products/*")
public class ProductsByCategoryMoreController extends AbstractController {
    private static final int SUBSTRING_INDEX = "/ajax/html/more/products".length();
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String categoryUrl = req.getRequestURI().substring(SUBSTRING_INDEX);
        List<Products> products = getProductService().listProductsByCategory(categoryUrl,getPage(req), Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("products",products);
        req.setAttribute("selectedCategoryUrl",categoryUrl);
        RoutingUtils.forwardToFragment("product-list.jsp", req, resp);
    }
}
