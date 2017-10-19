package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;

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

    public void callDataHandler(Attrs attrs, final Callback callback) {
        if (dataHandler != null) {
            DataHandlerProvider dataHandlerProvider = attrs.getDataHandlerProvider();
            dataHandlerProvider.getData(dataHandler, attrs, callback);
        } else {
            callback.call();
        }
    }

    public Object eval(Attrs attrs, String exp) {
        exp = exp.replaceAll("#\\{", "\\$\\{");
        SimpleContext context = new SimpleContext(new ElResolver());
        context.setVariable("attrs", factory.createValueExpression(attrs, Attrs.class));
        ValueExpression e = factory.createValueExpression(context, exp, Object.class);
        try {
            Object o = e.getValue(context);
            return o == null ? "" : o;
        } catch (Exception ex) {
            throw new RuntimeException("Evaluation error " + exp + " in block \"" + this.getClass().getName() + "\"", ex);
        }
    }

    public Iterable evalIt(Attrs attrs, String exp) {
        exp = exp.replaceAll("#\\{", "\\$\\{");
        Object o = eval(attrs, exp);
        if (o.getClass().isArray()) {
            Object[] o1 = (Object[]) o;
            o = Arrays.asList(o1);
        } else if (!(o instanceof Iterable)) {
            o = Collections.emptyList();
        }
        return (Iterable) o;
    }

    public JElement parent(Attrs attrs, String name, Block parent) {
        Block block = (Block) attrs.get("__parent__");
        if (block == null) {
           block = parent;
        }

        try {
            Method method = block.getClass().getDeclaredMethod(name+"Super", Attrs.class);
            return (JElement) method.invoke(block, attrs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new JBody();
    }

    protected Block self() {
        return this;
    }

    public interface Callback {
        void call();
    }

}
