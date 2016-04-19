package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

import java.util.Arrays;

/**
 * Created by aleph on 17.02.2016.
 */
public abstract class Block {

    private String name;
    private String parent;

    public abstract JElement body(Attrs attrs);

    public Object eval(Attrs attrs, String exp) {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression(exp);
        JexlContext jc = new MapContext();
        jc.set("attrs", attrs);
        return e.evaluate(jc);
    }

    public Iterable evalIt(Attrs attrs, String exp) {
        Object o = eval(attrs, exp);
        if (o.getClass().isArray()) {
            Object[] o1 = (Object[]) o;
            o = Arrays.asList(o1);
        }
        return (Iterable) o;
    }

}
