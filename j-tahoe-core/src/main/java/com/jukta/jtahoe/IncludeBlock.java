package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;

/**
 * @author Sergey Sidorov
 */
public class IncludeBlock extends Block {
    private String className;

    public IncludeBlock(String className) {
        this.className = className;
    }

    @Override
    public JElement body(Attrs attrs) {
        try {
            Block block = (Block) Class.forName(className, false, Thread.currentThread().getContextClassLoader()).newInstance();
            return block.body(attrs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
