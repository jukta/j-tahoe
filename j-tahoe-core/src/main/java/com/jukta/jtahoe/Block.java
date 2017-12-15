package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JBody;
import com.jukta.jtahoe.jschema.JElement;
import de.odysseus.el.ExpressionFactoryImpl;
import de.odysseus.el.util.SimpleContext;

import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public abstract class Block {
    private String parent;
    protected String dataHandler;
    protected static ExpressionFactory factory = new ExpressionFactoryImpl();
    private Block parentBlock;
    protected Map<String, Class> inners = new HashMap();
    protected Map<String, Class> aliases = new HashMap();

    public Block() {
        initDefs();
    }

    public JElement body(Attrs attrs) {
        init(attrs);
        final JBody el = new JBody();
        callDataHandler(attrs, () -> {
            beforeRendering(attrs);
            doBody(el, attrs);
            afterRendering(attrs, el);
        });
        return el;
    }

    protected void beforeRendering(Attrs attrs) {
        if (attrs.getBlockHandler() != null)
            attrs.getBlockHandler().before(getBlockType().getName(), attrs, this);
    }

    protected void afterRendering(Attrs attrs, JBody body) {
        if (attrs.getBlockHandler() != null)
            attrs.getBlockHandler().after(getBlockType().getName(), attrs, body, this);
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

    public Boolean evalBool(Attrs attrs, String exp) {
        Object res = eval(attrs, exp);
        if (res instanceof Boolean) {
            return (Boolean) res;
        } else if (res instanceof String) {
            return Boolean.valueOf((String) res);
        }
        return false;
    }

    public Iterable evalIt(Attrs attrs, String exp) {
//        exp = exp.replaceAll("#\\{", "\\$\\{");
        Object o = eval(attrs, exp);
        if (o.getClass().isArray()) {
            Object[] o1 = (Object[]) o;
            o = Arrays.asList(o1);
        } else if (!(o instanceof Iterable)) {
            o = Collections.emptyList();
        }
        return (Iterable) o;
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
        blockDef.setBlock(this);
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
        Class alias = block.aliases.get(className);
        if (alias != null) {
            return alias;
        }
        Class c = block.inners.get(className);
        if (c == null) {
            return findClass(className, block.parentBlock);
        }
        return c;
    }

    public Block create(String className, Attrs attrs) {
        Class c = findClass(className, this);
        if (c == null) {
            try {
                c = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e) {}
        }
        Block block ;
        if (c == null) {
            block = new NullBlock();
        } else {
            try {
                block = (Block) c.newInstance();
            } catch (Exception e) {
                block = new NullBlock();
            }
        }
        block.parentBlock = this;
        return block;
    }

}
