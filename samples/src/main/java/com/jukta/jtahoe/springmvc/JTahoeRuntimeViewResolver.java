package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.DirHandler;
import com.jukta.jtahoe.file.JTahoeResourceXml;
import com.jukta.jtahoe.file.JTahoeXml;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Created by Dmitriy Dobrovolskiy on 04.04.2016.
 *
 * @since *.*.*
 */
public class JTahoeRuntimeViewResolver implements ViewResolver {

    private String blocksFolder = "blocks";

    private ClassLoader classLoader;

    @Override
    public View resolveViewName(String s, Locale locale) throws Exception {
        return new JTahoeView(s, classLoader);
    }

    public void loadClasses() throws Exception {
        List<JTahoeXml> xmlFilesList = getXmlsFromResources();
        classLoader = new MemoryClassLoader(new DirHandler(new File("/")).getJavaFiles(xmlFilesList));
    }

    private List<JTahoeXml> getXmlsFromResources() throws IOException {
        List<JTahoeXml> xmlFilesList = new ArrayList<>();
        Enumeration<URL> en = getClass().getClassLoader().getResources("");
        while (en.hasMoreElements()) {
            URL url = en.nextElement();
            URLConnection urlConnection = url.openConnection();
            if (urlConnection instanceof JarURLConnection) {
                JarURLConnection urlcon = (JarURLConnection) urlConnection;
                try (JarFile jar = urlcon.getJarFile()) {
                    for (Enumeration<JarEntry> entries = jar.entries(); entries.hasMoreElements(); ) {
                        JarEntry entry = entries.nextElement();
                        String entryName = entry.getName();
                        if (entryName.startsWith(blocksFolder + "/") && entryName.endsWith(".xml")) {
                            JTahoeXml jTahoeXml = new JTahoeResourceXml(jar.getInputStream(entry), entryName.substring(0, entryName.lastIndexOf("/") + 1));
                            xmlFilesList.add(jTahoeXml);
                        }
                    }
                }
            }
        }
        return xmlFilesList;
    }

    public void setBlocksFolder(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }
}