package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

/**
 * Created by Dmitriy Dobrovolskiy on 26.04.2016.
 *
 * @since *.*.*
 */
public class TagAttrHandler extends AbstractHandler {
    public TagAttrHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
    }

    @Override
    public void end() {
        String name = parseExp(getAttrs().get("name"), true);
        String value = parseExp(getAttrs().get("value"), true);
        TagHandler parentTag = getParentTag(getParent());
        if (value == null) {
            getParent().appendCode("attrs" + parentTag.getVarName() + ".addAttr((String)" + name + ", null);\n");
        } else {
            getParent().appendCode("attrs" + parentTag.getVarName() + ".addAttr((String)" + name + ", (String)" + value + ");\n");
        }
    }

    private TagHandler getParentTag(AbstractHandler parent) {
        if (parent != null) {
            if (parent instanceof TagHandler) {
                return (TagHandler) parent;
            } else {
                return getParentTag(parent.getParent());
            }
        } else {
            throw new RuntimeException("tagAttr must be child of tag element");
        }
    }
}
