package net.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.sql.SQLException;
import java.util.Properties;

import javax.servlet.ServletContext;

import net.service.OrderService;
import net.service.ProductService;
import net.service.SocialService;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceManager {
    //creates a singleton object ServiceManager which gives it to the controller on request, through it you can work with services
    public static ServiceManager getInstance(ServletContext context){
        ServiceManager instance = (ServiceManager) context.getAttribute("SERVICE_MANAGER");
        if (instance == null) {
            instance = new ServiceManager(context);
            context.setAttribute("SERVICE_MANAGER", instance);
        }
        return instance;
    }

    public ProductService getProductService() {
        return productService;
    }
    public OrderService getOrderService() {
        return orderService;
    }

    public SocialService getSocialService() {
        return socialService;
    }

    public String getApplicationProperty(String key) {
        String value = applicationProperties.getProperty(key);
        if(value.startsWith("${sysEnv.")) {
            String keyVal = value.replace("${sysEnv.", "").replace("}", "");
            value = System.getProperty(keyVal);
        }
        return value;
    }
    public void close() {
        try {
            dataSource.close();
        } catch (SQLException e) {
        }
    }

    private final Properties applicationProperties = new Properties();
    private final ProductService productService;
    private final OrderService orderService;
    private final BasicDataSource dataSource;
    private final SocialService socialService;

    //the constructor creates singleton objects OrdServImpl and ProdServImpl you can work with them only through the ServiceManager, and also loads the configuration
    private ServiceManager(ServletContext context){
        loadApplicationProperties();
        dataSource = createDataSource();
        productService = new ProductServiceImpl(dataSource);
        orderService = new OrderServiceImpl(dataSource,this);
        socialService = new FacebookSocialService(this);
    }
    //initialize database data from properties and return dataSource
    private BasicDataSource createDataSource() {

            BasicDataSource dataSource = new BasicDataSource();
            dataSource.setDefaultAutoCommit(false);
            dataSource.setRollbackOnReturn(true);
            dataSource.setDriverClassName(getApplicationProperty("db.driver"));
            dataSource.setUrl(getApplicationProperty("db.url"));
            dataSource.setUsername(getApplicationProperty("db.username"));
            dataSource.setPassword(getApplicationProperty("db.password"));
            dataSource.setInitialSize(Integer.parseInt(getApplicationProperty("db.pool.initSize")));
            dataSource.setMaxTotal(Integer.parseInt(getApplicationProperty("db.pool.maxSize")));

        return dataSource;
    }
    //loads application configuration from application properties
    private void loadApplicationProperties(){
        try(InputStream in = ServiceManager.class.getClassLoader().getResourceAsStream("application.properties")) {
            applicationProperties.load(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
