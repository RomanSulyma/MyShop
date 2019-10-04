package net.servlet.ajax;

import net.Constants;
import net.entity.Products;
import net.form.SearchForm;
import net.servlet.AbstractController;
import net.util.RoutingUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

//search request ajax controller, processes the search string and pagination for not
@WebServlet("/ajax/html/more/search")
public class SearchResultMoreController extends AbstractController {
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SearchForm sf = createSearchForm(req);

        List<Products> products = getProductService().listProductsBySearchForm(sf,getPage(req), Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("products",products);
        RoutingUtils.forwardToFragment("product-list.jsp", req, resp);
    }
}
