package net.servlet.page;

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

//allows you to work with all products through the ServiceManager, processes all products
@WebServlet("/products")
public class AllProductsController extends AbstractController {
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        List<Products> products = getProductService().listAllProducts(1, Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("products",products);
        int totalCount = getProductService().countAllProducts();
        req.setAttribute("pageCount",getPageCount(totalCount,Constants.MAX_PRODUCTS_PER_HTML_PAGE));
        RoutingUtils.forwardToPage("products.jsp", req, resp);
    }
}
