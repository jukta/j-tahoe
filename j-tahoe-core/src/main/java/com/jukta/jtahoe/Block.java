package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.lang.reflect.Method;
import java.util.*;

public abstract class Block {
    private String parent;
    protected String dataHandler;
    private static ExpressionFactory factory = new ExpressionFactoryImpl();
    private Block parentBlock;
    protected Map<String, Class> inners = new HashMap();

    public Block() {
        initDefs();
    }

    public JElement body(Attrs attrs) {
        init(attrs);
        final JBody el = new JBody();
        callDataHandler(attrs, () -> {
            if (attrs.getBlockHandler() != null)
                attrs.getBlockHandler().before(getBlockType().getName(), attrs, this);

            doBody(el, attrs);

            if (attrs.getBlockHandler() != null)
                attrs.getBlockHandler().after(getBlockType().getName(), attrs, el, this);
        });
        return el;

    }

    protected void doBody(JBody el, Attrs attrs) {

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

    public Class getBlockType() {
        return getBlockType(this.getClass());
    }

    public static Class getBlockType(Class c) {
        if (c.isAnonymousClass()) {
            return getBlockType(c.getSuperclass());
        }
        return c;
    }

    public interface Callback {
        void call();
    }

    private Map<String, BlockDef> defs = new HashMap<>();

    public Block addDef(String name, BlockDef blockDef) {
        BlockDef prev = defs.get(name);
        if (prev != null) {
            blockDef.setParent(prev);
        }
        defs.put(name, blockDef);
        return this;
    }

    public JElement def(String name, Attrs attrs) {
        BlockDef blockDef = defs.get(name);
        if (blockDef == null) return null;
        return blockDef.doDef(attrs, this);
    }

    public void initDefs() {};

    private Class findClass(String className, Block block) {
        if (block == null) return null;
        Class c = block.inners.get(className);
        if (c == null) {
            return findClass(className, block.parentBlock);
        }
        return c;
    }

    public Block create(String className, Attrs attrs) {
        try {
            Class c = findClass(className, this);
            if (c == null) {
                c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            }
            Block block = (Block) c.newInstance();
            block.parentBlock = this;
            return block;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
