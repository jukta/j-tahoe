package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by aleph on 18.02.2016.
 */
public class AliasHandler extends AbstractHandler {

    public AliasHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String name = processPrefix(getAttrs().get("name"));
        String alias = processPrefix(getAttrs().get("alias"));
        BlockHandler b = getBlock(false);
        b.addAlias(alias, name);
    }

}
