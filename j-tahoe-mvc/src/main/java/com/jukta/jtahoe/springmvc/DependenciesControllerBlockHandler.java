package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockHandler;
import com.jukta.jtahoe.BuildId;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.*;

public class DependenciesControllerBlockHandler implements BlockHandler {

    private Map<String, Dependencies> deps = new HashMap<>();
    private boolean addAllDeps;

    @PostConstruct
    public void load() throws IOException {
        String autoDeps = System.getProperty("auto.dependencies");
        addAllDeps = "all".equals(autoDeps);
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        Resource[] r = resolver.getResources("classpath*:/jtahoe.properties");
        for (Resource r1 : r) {
            Properties p = new Properties();
            p.load(r1.getInputStream());

            List<String> js = new ArrayList<>();
            List<String> css = new ArrayList<>();

            String jsProp = p.getProperty("dependencies.js");
            if (jsProp != null) {
                js.addAll(Arrays.asList(jsProp.split(";")));
            }

            String cssProp = p.getProperty("dependencies.css");
            if (cssProp != null) {
                css.addAll(Arrays.asList(cssProp.split(";")));
            }

            Dependencies d = new Dependencies(p.getProperty("lib.id"), js, css);
            deps.put(d.getLibId(), d);
        }
    }


    @Override
    public void startRendering(Attrs attrs) {

    }

    @Override
    public void before(String blockName, Attrs attrs, Block block) {
        if (!addAllDeps && block.getClass().isAnnotationPresent(BuildId.class)) {
            Set<String> depIds = (Set<String>) attrs.getAttribute("__deps");
            if (depIds == null) {
                depIds = new HashSet<>();
                attrs.setAttribute("__deps", depIds);
            }

            BuildId buildId = block.getClass().getAnnotation(BuildId.class);
            String id = buildId.value();
            depIds.add(id);
        }
    }

    @Override
    public void after(String blockName, Attrs attrs, JElement jElement, Block block) {
        if (block instanceof Script && ((Script) block).isAutoMode()) {
            attrs.setAttribute("__scriptDepsBody", jElement);
        } else if (block instanceof Stylesheet && ((Stylesheet) block).isAutoMode()) {
            attrs.setAttribute("__styleDepsBody", jElement);
        }
    }

    @Override
    public void stopRendering(Attrs attrs, JElement jElement) {
        Set<String> depIds = (Set<String>) attrs.getAttribute("__deps");
        if (addAllDeps) {
            depIds = deps.keySet();
        }
        if (depIds == null) {
            return;
        }

        List<String> entries = new ArrayList<>();

        JBody jBody = (JBody) attrs.getAttribute("__scriptDepsBody");
        if (jBody != null) {
            for (String id : depIds) {
                Dependencies d = deps.get(id);
                for (String s : d.getJs()) {
                    if (!entries.contains(s)) {
                        entries.add(s);
                        jBody.addElement(Script.getScriptElement(s));
                    }
                }
            }
            jBody.addElement(Script.getScriptElement("/app.js"));
        }

        entries.clear();
        jBody = (JBody) attrs.getAttribute("__styleDepsBody");
        if (jBody != null) {
            for (String id : depIds) {
                Dependencies d = deps.get(id);
                for (String s : d.getCss()) {
                    if (!entries.contains(s)) {
                        entries.add(s);
                        jBody.addElement(Stylesheet.getStylesheetElement(s));
                    }
                }
            }
            jBody.addElement(Stylesheet.getStylesheetElement("/app.css"));
        }

    }

    public static class Dependencies {
        private String libId;
        private List<String> js;
        private List<String> css;

        public Dependencies(String libId, List<String> js, List<String> css) {
            this.libId = libId;
            this.js = js;
            this.css = css;
        }

        public String getLibId() {
            return libId;
        }

        public List<String> getJs() {
            return js;
        }

        public List<String> getCss() {
            return css;
        }

    }

}
