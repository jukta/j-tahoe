package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.DirHandler;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.util.Locale;

/**
 * @author Sergey Sidorov
 */
public class JTahoeViewResolver implements ViewResolver {

    private DirHandler dirHandler;
    private ClassLoader classLoader;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new JTahoeView(s, classLoader);
    }

    public void setDirHandler(DirHandler dirHandler) {
        this.dirHandler = dirHandler;
    }

    public void loadClasses() throws Exception {
        if (dirHandler != null) {
            classLoader = new MemoryClassLoader(dirHandler.getFiles());
        }
    }
}
