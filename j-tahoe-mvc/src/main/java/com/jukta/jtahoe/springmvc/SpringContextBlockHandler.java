package com.jukta.jtahoe.springmvc;

import com.jukta.jtahoe.Attrs;
import com.jukta.jtahoe.Block;
import com.jukta.jtahoe.BlockHandler;
import com.jukta.jtahoe.jschema.JElement;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;

public class SpringContextBlockHandler implements BlockHandler {

    private List<BlockHandler> handlers;

    public SpringContextBlockHandler(ApplicationContext applicationContext) {
        handlers = new ArrayList<>(applicationContext.getBeansOfType(BlockHandler.class).values());
    }

    @Override
    public void startRendering(Attrs attrs) {
        handlers.forEach(blockHandler -> blockHandler.startRendering(attrs));
    }

    @Override
    public void before(String blockName, Attrs attrs, Block block) {
        handlers.forEach(blockHandler -> blockHandler.before(blockName, attrs, block));
    }

    @Override
    public void after(String blockName, Attrs attrs, JElement jElement, Block block) {
        handlers.forEach(blockHandler -> blockHandler.after(blockName, attrs, jElement, block));
    }

    @Override
    public void stopRendering(Attrs attrs, JElement jElement) {
        handlers.forEach(blockHandler -> blockHandler.stopRendering(attrs, jElement));
    }
}
