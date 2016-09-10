package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.Arrays;

/**
 * Created by aleph on 17.02.2016.
 */
public abstract class Block {
    private String name;
    private String parent;
    protected String dataHandler;
    private static ExpressionFactory factory = new ExpressionFactoryImpl();

    public JElement body(Attrs attrs) {
        init(attrs);
        return new JBody();
    }

    public void init(Attrs attrs) {};

    public void callDataHandler(Attrs attrs) {
        if (dataHandler != null) {
            DataHandlerProvider dataHandlerProvider = attrs.getDataHandlerProvider();
            attrs.setAll(dataHandlerProvider.getData(dataHandler, attrs));
        }
    }

    public Object eval(Attrs attrs, String exp) {
        SimpleContext context = new SimpleContext(new ElResolver());
        context.setVariable("attrs", factory.createValueExpression(attrs, Attrs.class));
        ValueExpression e = factory.createValueExpression(context, "${" + exp + "}", Object.class);
        try {
            Object o = e.getValue(context);
            return o == null ? "" : o;
        } catch (Exception ex) {
            throw new RuntimeException("Evaluation error ${" + exp + "} in block \"" + this.getClass().getName() + "\"", ex);
        }
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
