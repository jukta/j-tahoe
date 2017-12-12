package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JAttrs;
import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JTag;
import com.jukta.jtahoe.selector.CompositeSelector;
import de.odysseus.el.util.SimpleContext;

import javax.el.ValueExpression;
import java.util.Set;

/**
 * @since 1.0
 */
public class Morpher extends NullBlock {

    @Override
    public void doBody(JBody __258, Attrs attrs) {
        String selector = (String) attrs.get("selector");
        String operator = (String) attrs.get("operator");
        CompositeSelector sel = CompositeSelector.parse(selector);
        super.doBody(__258, attrs);
        Set<JTag> tagSet =  sel.select(__258);


        operator = "#{op." + operator + "}";
        SimpleContext context = new SimpleContext();
        context.setVariable("op", factory.createValueExpression(new M(tagSet), M.class));
        ValueExpression e = factory.createValueExpression(context, operator, String.class);
        try {
            e.getValue(context);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class M {
        private Set<JTag> tags;

        M(Set<JTag> tags) {
            this.tags = tags;
        }

        public void addClass(String className) {
            for(JTag tag : tags) {
                JAttrs a = tag.getAttrs();
                if (a == null) {
                    a = new JAttrs();
                    tag.setAttrs(a);
                }
                String cl = a.getAttrs().get("class");
                if (cl == null) cl = "";
                cl += " " + className;
                a.addAttr("class", cl.trim());
            }
        }
    }

}
