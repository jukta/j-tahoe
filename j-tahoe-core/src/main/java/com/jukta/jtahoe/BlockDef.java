package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;

/**
 * Created by Sergey on 11/7/2017.
 */
public abstract class BlockDef {

    private BlockDef parent;

    public BlockDef getParent() {
        return parent;
    }

    public void setParent(BlockDef parent) {
        this.parent = parent;
    }

    public JElement parent(Attrs attrs, Block b) {
        return parent == null ? new JBody() : parent.doDef(attrs, b);
    }

    public abstract JElement doDef(Attrs attrs, Block b);

}
