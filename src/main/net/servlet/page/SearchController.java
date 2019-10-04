package net.servlet.page;

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

//processes the search string and pagination for it
@WebServlet("/search")
public class SearchController extends AbstractController {
    private static final long serialVersionUID = -4385792519039493271L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        SearchForm sf = createSearchForm(req);

        List<Products> products = getProductService().listProductsBySearchForm(sf,1,Constants.MAX_PRODUCTS_PER_HTML_PAGE);
        req.setAttribute("products",products);
        int totalCount = getProductService().countProductsBySearchForm(sf);
        req.setAttribute("pageCount",getPageCount(totalCount,Constants.MAX_PRODUCTS_PER_HTML_PAGE));

        req.setAttribute("productCount", totalCount);
        req.setAttribute("searchForm",sf);
        RoutingUtils.forwardToPage("search-result.jsp", req, resp);
    }
}
