package net.service.impl;

import net.entity.Category;
import net.entity.Producers;
import net.entity.Products;
import net.exception.InternalServerErrorException;
import net.form.SearchForm;
import net.jdbc.JDBCUtils;
import net.jdbc.ResultSetHandler;
import net.jdbc.ResultSetHandlerFactory;
import net.jdbc.SearchQuery;
import net.service.ProductService;
import org.slf4j.Logger;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//implementation of work with products
public class ProductServiceImpl implements ProductService {


    private static final ResultSetHandler<List<Products>> productsRSH = ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCT_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<Category>> categoryListResultSetHandler = ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.CATEGORY_RESULT_SET_HANDLER);
    private final ResultSetHandler<List<Producers>> producerListResultSetHandler = ResultSetHandlerFactory.getListResultSetHandler(ResultSetHandlerFactory.PRODUCER_RESULT_SET_HANDLER);
    private final ResultSetHandler<Integer> countResultSetHandler = ResultSetHandlerFactory.getCountResultSetHandler();

    private final DataSource dataSource;
    public ProductServiceImpl(DataSource dataSource) {
        super();
        this.dataSource = dataSource;
    }
    //makes a request to the database to select all products
   @Override
    public List<Products> listAllProducts(int page, int limit) {
        try(Connection c = dataSource.getConnection()){
            int offset = (page-1) * limit;
            return JDBCUtils.select(c,"select p.*, c.name as category, pr.name as producer from product p , producer pr , category c where c.id = p.id_category and pr.id = p.id_producer limit ? offset ?",productsRSH,limit,offset);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Can't execute sql query" + e.getMessage() , e);
        }
    }
    //makes a request to the database to select products by category
    @Override
    public List<Products> listProductsByCategory(String categoryURL, int page, int limit) {
        try(Connection c = dataSource.getConnection()){
            int offset = (page-1) * limit;
            return JDBCUtils.select(c,"select p.*, c.name as category, pr.name as producer from product p, category c, producer pr where c.url = ? and pr.id=p.id_producer and c.id=p.id_category order by p.id limit ? offset ?",productsRSH,categoryURL,limit,offset);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Can't execute sql query" + e.getMessage() , e);
        }
    }
    //makes a query to the database to select categories for aside.jsp
       @Override
    public List<Category> listAllCategories() {
           try(Connection c = dataSource.getConnection()){
                return JDBCUtils.select(c,"select * from category order by id", categoryListResultSetHandler);
           } catch (SQLException e) {
               e.printStackTrace();
               throw new InternalServerErrorException("Can't execute sql query" + e.getMessage() , e);
           }
    }
    //makes a query to the database for a selection of manufacturers for aside.jsp
    @Override
    public List<Producers> listAllProducers() {
        try(Connection c = dataSource.getConnection()){
            return JDBCUtils.select(c,"select * from producer order by name", producerListResultSetHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Can't execute sql query" + e.getMessage() , e);
        }
    }
    //select from the database of the number of all products for pagination
    @Override
    public int countAllProducts() {
        try(Connection c = dataSource.getConnection()){
            return JDBCUtils.select(c,"select count(*) from product",countResultSetHandler);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Can't execute sql query" + e.getMessage() , e);
        }
    }
    //selection from the database of the number of products by category for pagination
    @Override
    public int countProductsByCategory(String categoryUrl) {

        try(Connection c = dataSource.getConnection()){
            return JDBCUtils.select(c,"select count(p.*) from product p, category c where c.id=p.id_category and c.url=?",countResultSetHandler,categoryUrl);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new InternalServerErrorException("Can't execute sql query" + e.getMessage() , e);
        }
    }
    //selection from the product database for the search form based on the parameters arrived
    @Override
    public List<Products> listProductsBySearchForm(SearchForm form, int page, int limit) {
        try (Connection c = dataSource.getConnection()) {
            int offset = (page - 1) * limit;
            SearchQuery sq = buildSearchQuery("p.*, c.name as category, pr.name as producer", form);
            sq.getSql().append(" order by p.id limit ? offset ?");
            sq.getParams().add(limit);
            sq.getParams().add(offset);
            return JDBCUtils.select(c, sq.getSql().toString(), productsRSH, sq.getParams().toArray());
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }
    //method of constructing a query string with adding to the request all the parameters that came from the search form
    protected SearchQuery buildSearchQuery(String selectFields, SearchForm form) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder("select ");
        sql.append(selectFields).append(" from product p, category c, producer pr where pr.id=p.id_producer and c.id=p.id_category and (p.name ilike ? or p.description ilike ?)");
        params.add("%" + form.getQuery() + "%");
        params.add("%" + form.getQuery() + "%");
        JDBCUtils.populateSqlAndParams(sql, params, form.getCategories(), "c.id = ?");
        JDBCUtils.populateSqlAndParams(sql, params, form.getProducers(), "pr.id = ?");
        JDBCUtils.populateSqlAndParams(sql, params, form.getPrice(), "p.price < ?");

        return new SearchQuery(sql, params);
    }
    //Selection of the number of products for pagination when requesting through the search form
    @Override
    public int countProductsBySearchForm(SearchForm form) {
        try (Connection c = dataSource.getConnection()) {
            SearchQuery sq = buildSearchQuery("count(*)", form);
            return JDBCUtils.select(c, sq.getSql().toString(), countResultSetHandler, sq.getParams().toArray());
        } catch (SQLException e) {
            throw new InternalServerErrorException("Can't execute SQL request: " + e.getMessage(), e);
        }
    }
}
