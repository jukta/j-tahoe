package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.DirHandler;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver {

    private DirHandler dirHandler;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new JTahoeView(s);
    }

    public void setDirHandler(DirHandler dirHandler) {
        this.dirHandler = dirHandler;
    }

//    public void loadClasses() throws IllegalAccessException, ParserConfigurationException, IOException, InstantiationException, SAXException, ClassNotFoundException {
//        if (dirHandler != null) {
//            dirHandler.loadCompiledSources();
//        }
//    }
}
