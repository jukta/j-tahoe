package com.jukta.jtahoe;

import com.jukta.jtahoe.jschema.JElement;
import javassist.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Sidorov
 */
public class IncludeBlock extends Block {
    private static Map<String, Class> classMap = new HashMap<>();
    private String className;
    private String[] defs;

    public IncludeBlock(String className, String[] defs) {
        this.className = className;
        this.defs = defs;
    }

    @Override
    public JElement body(Attrs attrs) {
//        String s = "public void def() { super.def();}";
//
//        int hash = (s + className).hashCode();
//        String clName = "Inc_" + hash;
//        Class aClass = classMap.get(clName);
        try {
//            if (aClass == null) {
//                ClassPool cp = new ClassPool(false);
//                cp.appendClassPath(new ClassPath());
//                CtClass ctclass = cp.get(className);
//                CtClass hello = cp.makeClass(clName);
//                hello.setSuperclass(ctclass);
//                CtMethod newmethod = CtNewMethod.make(s, hello);
//                hello.addMethod(newmethod);
//                aClass = hello.toClass();
//                classMap.put(clName, aClass);
//            }

            Class aClass = Class.forName(className, true, Thread.currentThread().getContextClassLoader());

            Block b = (Block) aClass.newInstance();
            return b.body(attrs);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
