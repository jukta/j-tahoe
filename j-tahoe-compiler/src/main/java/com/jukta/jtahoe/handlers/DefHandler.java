package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.io.StringWriter;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by aleph on 20.02.2016.
 */
public class DefHandler extends AbstractHandler {

    String body = "";
    protected String name;
    private boolean defaultDef = false;
    private Set<String> superDefs = new HashSet<>();

    public DefHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
        this.name = getAttrs().get("name");
    }

    public void addSuperMethod(String defName) {
        superDefs.add(defName);
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
        BlockHandler blockHandler = getBlock(true);
        BlockHandler blockHandlerInt = getBlock(false);

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

        if (!defaultDef && !blockHandler.equals(blockHandlerInt)) {
            def = "public JElement " + defNameInt + "(final Attrs _attrs" + getVarName()+ ")";

            fw = new StringWriter();
            fw.write("{");
            fw.write("attrs.set(\"__parent__\", this);");
            fw.write("return " + el + ";");
            fw.write("}");

            blockHandlerInt.addDef(def, fw.toString());

            if (superDefs.contains(defNameInt)) {
                def = "public JElement " + defNameInt + "Super(final Attrs _attrs" + getVarName() + ")";
                fw = new StringWriter();
                fw.write("{ return super." + defNameInt + "(attrs); }");
                blockHandlerInt.addDef(def, fw.toString());
            }
        }

    }
}
