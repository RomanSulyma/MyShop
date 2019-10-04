package net.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import net.Constants;

import net.service.impl.ServiceManager;

@WebListener
public class IShopApplicationListener implements ServletContextListener {
    private ServiceManager serviceManager;

    //ServiceManager will be created at application startup and written to the servlet context, writes a list of categories and producers for aside.jsp, also logs actions
    @Override
    public void contextInitialized(ServletContextEvent sce) {
            serviceManager = ServiceManager.getInstance(sce.getServletContext());
            sce.getServletContext().setAttribute(Constants.CATEGORY_LIST, serviceManager.getProductService().listAllCategories());
            sce.getServletContext().setAttribute(Constants.PRODUCER_LIST, serviceManager.getProductService().listAllProducers());

    }
    //on destruction, it will be closed, as well as all the resources that it used will be closed
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        serviceManager.close();
    }
}

