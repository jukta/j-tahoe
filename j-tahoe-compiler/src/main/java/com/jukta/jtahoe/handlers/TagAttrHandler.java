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
        String name = getAttrs().get("name");
        String value = getAttrs().get("value");
//        TagHandler parentTag = getParentTag(getParent());
        AbstractHandler parentTag = getParent();
        if (parentTag != null && parentTag instanceof TagHandler) {
            ((TagHandler)parentTag).addAttribute(name, value);
        } else {
            throw new RuntimeException("tagAttr must be child of tag element");
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
