package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.io.StringWriter;

/**
 * Created by aleph on 20.02.2016.
 */
public class DefHandler extends AbstractHandler {

    String body = "";
    protected String name;
    private boolean defaultDef = false;

    public DefHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
        this.name = getAttrs().get("name");
    }

    public void setDefaultDef(boolean defaultDef) {
        this.defaultDef = defaultDef;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDefName() {
        return name == null ? "def_" + getBlock(true).getBlockName(): "def_" + name;
    }

    public void appendCode(String code) {
        body += code;
    }

    @Override
    public void addElement(String element) {
        body += getVarName()+".addElement(" + element + ");\n";
    }

    @Override
    public void end() {
//        String attrs = "new com.jukta.jtahoe.Attrs()";
//        for (String s : getAttrs().keySet()) {
//            attrs += ".set(\"" + s + "\", \"" + getAttrs().get(s) + "\")";
//        }

        BlockHandler blockHandler = getBlock(true);
        BlockHandler blockHandlerInt = getBlock(false);

        if (getBlock(false).getBlockName().equals("AdvancedDefInheritance_A")) {
            System.out.println();
        }

//        if (name != null && name.equals("aaa")) {
//            System.out.println();
//        }

        String defName = getDefName();
        String defNameInt = name == null ? "def_" + blockHandlerInt.getBlockName(): "def_" + name;

        String el;
        if (getParent() instanceof DefHandler) {
            el = "this." + defName + "(attrs)";
        } else {
            el = "((" + blockHandler.getBlockName() + ")_" + blockHandler.getBlockName() + ")." + defName + "(attrs)";
        }
        getParent().addElement(el);

        String def;
        if (blockHandler instanceof FuncHandler || (defaultDef && !blockHandler.equals(blockHandlerInt))) {
            def = "public JElement " + defName + "(final Attrs _attrs" + getVarName()+ ")";
        } else {
            def = "public JElement " + defName + "(final Attrs attrs)";
        }
        StringWriter fw = new StringWriter();
        fw.write("{");
        fw.write("JBody " + getVarName() + " = new JBody();\n");
        fw.write(body);
        fw.write("return " + getVarName() + ";");
        fw.write("}");

        if (!defaultDef && !blockHandler.equals(blockHandlerInt)) {
            blockHandler.addDef(def, fw.toString());
        } else {
            blockHandlerInt.addDef(def, fw.toString());
        }

//        if (blockHandler.equals(blockHandlerInt) && blockHandler.getParentBlock() != null) {
//            def = "public JElement " + defName + "Super(final Attrs attrs)";
//            fw = new StringWriter();
//            fw.write("{ return super." + defName + "(attrs); }");
//            blockHandler.addDef(def, fw.toString());
//        }

        if (!defaultDef && !blockHandler.equals(blockHandlerInt)) {
            def = "public JElement " + defNameInt + "(final Attrs _attrs" + getVarName()+ ")";

            fw = new StringWriter();
            fw.write("{");
//            fw.write("JBody " + getVarName() + " = new JBody();\n");
//            fw.write(el + ";");
//            fw.write("return " + getVarName() + ";");
            fw.write("attrs.set(\"__parent__\", this);");
            fw.write("return " + el + ";");
            fw.write("}");

            blockHandlerInt.addDef(def, fw.toString());

            def = "public JElement " + defNameInt + "Super(final Attrs _attrs" + getVarName()+ ")";
            fw = new StringWriter();
            fw.write("{ return super." + defNameInt + "(attrs); }");
            blockHandlerInt.addDef(def, fw.toString());
        }

    }
}
