package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;

public class NullBlock extends Block {

    public void initDefs() {
        this.addDef("def_"+getBlockType().getSimpleName(), new BlockDef() {
            public JElement doDef(final Attrs attrs, Block block__259) {
                return new JBody();
            }
        });
    }

    public void doBody(final JBody __258, final Attrs attrs) {
        __258.addElement(this.def("def_"+getBlockType().getSimpleName(), attrs));

    }
}