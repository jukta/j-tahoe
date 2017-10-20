package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.*;
import com.jukta.jtahoe.ArtifactInfo;
import com.jukta.jtahoe.gen.*;
import com.jukta.jtahoe.jschema.JElement;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class JTahoeView implements View {
    private String contentType = "text/html;charset=UTF-8";
    private DataHandlerProvider handlerProvider;

    private String viewName;
    private BlockFactory blockFactory;
    private LibraryMetaController libraryMetaController;

    public JTahoeView(String viewName, BlockFactory blockFactory, LibraryMetaController libraryMetaController) {
        this.viewName = viewName;
        this.blockFactory = blockFactory;
        this.libraryMetaController = libraryMetaController;
    }

    public void setHandlerProvider(DataHandlerProvider handlerProvider) {
        this.handlerProvider = handlerProvider;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public void render(Map<String, ?> map, HttpServletRequest httpservletrequest, HttpServletResponse httpservletresponse) throws Exception {
        Thread.currentThread().setContextClassLoader(blockFactory.getClassLoader());
        Block block = blockFactory.create(viewName);
        Attrs attrs = new Attrs();
        attrs.setDataHandlerProvider(handlerProvider);
        for (Map.Entry<String, ?> entry : map.entrySet()) {
            attrs.set(entry.getKey(), entry.getValue());
        }

        attrs.setAttribute("session", httpservletrequest.getSession());
        attrs.setAttribute("request", httpservletrequest);

        attrs.setBlockHandler(new BlockHandler() {
            @Override
            public void before(String blockName, Attrs attrs, Block block) {
                if (block.getClass().isAnnotationPresent(ArtifactInfo.class)) {
                    ArtifactInfo info = block.getClass().getDeclaredAnnotation(ArtifactInfo.class);

                    com.jukta.jtahoe.gen.ArtifactInfo i = new com.jukta.jtahoe.gen.ArtifactInfo(info.groupId(), info.artifactId(), info.version());
                    System.out.println(i);
                    for (com.jukta.jtahoe.gen.ArtifactInfo i1 : libraryMetaController.getDependencies(i)) {
                        System.out.println("\t" + i1);
                    }
                }
            }

            @Override
            public void after(String blockName, Attrs attrs, JElement jElement, Block block) {

            }
        });

        JElement el = block.body(attrs);

        handlerProvider.await();

        httpservletresponse.setCharacterEncoding("UTF-8");
        httpservletresponse.setContentType(getContentType());
        httpservletresponse.getWriter().write(el.toHtml());
    }

}
