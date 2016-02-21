package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;
import org.apache.commons.jexl2.Expression;
import org.apache.commons.jexl2.JexlContext;
import org.apache.commons.jexl2.JexlEngine;
import org.apache.commons.jexl2.MapContext;

/**
 * Created by aleph on 17.02.2016.
 */
public abstract class Block {

    private String name;
    private String parent;

    public abstract JElement body(Attrs attrs);

    public Object eval(Attrs attrs, String exp) {
        JexlEngine jexl = new JexlEngine();
        Expression e = jexl.createExpression( exp );
        JexlContext jc = new MapContext();
        jc.set("attrs", attrs );
        return e.evaluate(jc);
    }

}
