package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.gen.DirHandler;
import com.jukta.jtahoe.resource.ResourceType;
import com.jukta.jtahoe.resource.Resources;
import com.jukta.jtahoe.file.JTahoeXml;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Locale;

/**
 * Created by Dmitriy Dobrovolskiy on 04.04.2016.
 *
 * @since *.*.*
 */
public class JTahoeRuntimeViewResolver implements ViewResolver, InitializingBean, ApplicationContextAware {

    private String blocksFolder = "blocks";
    private ClassLoader classLoader;
    private ApplicationContext applicationContext;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        JTahoeView view = new JTahoeView(s, classLoader);
        view.setApplicationContext(applicationContext);
        return view;
    }

    public void loadClasses() throws Exception {
        List<JTahoeXml> xmlFilesList = getXmlsFromResources();
        classLoader = new MemoryClassLoader(new DirHandler(new File("/")).getJavaFiles(xmlFilesList));
    }

    private List<JTahoeXml> getXmlsFromResources() throws IOException, URISyntaxException {
        return new Resources(blocksFolder).getFiles(ResourceType.XML);
    }

    public void setBlocksFolder(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        loadClasses();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}