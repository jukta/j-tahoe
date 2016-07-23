package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.BlockFactory;
import com.jukta.jtahoe.gen.xml.XmlBlockModelProvider;
import com.jukta.jtahoe.model.NodeProcessor;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import javax.tools.JavaFileObject;
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
    private BlockFactory blockFactory;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        JTahoeView view = new JTahoeView(s, blockFactory, classLoader);
        view.setApplicationContext(applicationContext);
        return view;
    }

    public void loadClasses() throws Exception {
        List<JavaFileObject> javaFileObjects = new NodeProcessor().process(new XmlBlockModelProvider(blocksFolder));
        classLoader = new MemoryClassLoader(javaFileObjects);
        blockFactory = new BlockFactory(classLoader);
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