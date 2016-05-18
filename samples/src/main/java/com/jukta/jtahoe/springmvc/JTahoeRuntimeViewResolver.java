package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.DirHandler;
import com.jukta.jtahoe.file.JTahoeResourceXml;
import com.jukta.jtahoe.file.JTahoeXml;
import com.jukta.jtahoe.loader.MemoryClassLoader;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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

    private List<JTahoeXml> getXmlsFromResources() throws IOException, URISyntaxException {
        List<JTahoeXml> xmlFilesList = new ArrayList<>();
        URL url = getClass().getClassLoader().getResource(blocksFolder);
        getFiles(xmlFilesList, new File(url.getFile()));
        return xmlFilesList;
    }

    private void getFiles(List<JTahoeXml> xmlFilesList, File file) throws IOException, URISyntaxException {
        if (file.isDirectory()) {
            for (File entry : file.listFiles()) {
                getFiles(xmlFilesList, entry);
            }
        } else {
            String entryName = file.getName();
            if (entryName.endsWith(".xml")) {
                JTahoeXml jTahoeXml = new JTahoeResourceXml(new FileInputStream(file), getClass().getClassLoader().getResource(blocksFolder).toURI().relativize(file.getParentFile().toURI()).toString());
                xmlFilesList.add(jTahoeXml);
            }
        }
    }

    public void setBlocksFolder(String blocksFolder) {
        this.blocksFolder = blocksFolder;
    }
}