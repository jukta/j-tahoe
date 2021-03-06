package com.jukta.jtahoe.taglib;

import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.DataHandlerProvider;
import com.jukta.jtahoe.RuntimeBlockFactory;
import com.jukta.jtahoe.gen.xml.XthBlockModelProvider;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * @since 1.0
 */
public class ContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext context = sce.getServletContext();
            BlockFactory blockFactory = new RuntimeBlockFactory(new XthBlockModelProvider());
            context.setAttribute("_jTahoe_blockFactory", blockFactory);

            String dataHandlerProviderClass = context.getInitParameter("dataHandlerProviderClass");
            if (dataHandlerProviderClass != null) {
                DataHandlerProvider dataHandlerProvider = (DataHandlerProvider) Class.forName(dataHandlerProviderClass).newInstance();
                context.setAttribute("_jTahoe_dataHandlerProvider", dataHandlerProvider);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {

    }
}
