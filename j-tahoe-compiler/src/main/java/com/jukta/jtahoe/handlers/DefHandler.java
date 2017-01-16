package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.model.NamedNode;

import java.io.StringWriter;

/**
 * Created by aleph on 20.02.2016.
 */
public class DefHandler extends AbstractHandler {

    String body = "";
    private String name;

    public DefHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
        this.name = getAttrs().get("name");
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getDefName() {
        return name;
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

        BlockHandler blockHandler = getBlock();

        String defName = name == null ? "def_" : "def_" + name;

        String el;
        if (getParent() instanceof DefHandler) {
            el = "this." + defName + "(attrs)";
        } else {
            el = "((" + blockHandler.getBlockName() + ")_" + blockHandler.getBlockName() + ")." + defName + "(attrs)";
        }
        getParent().addElement(el);

        String def;
        if (blockHandler instanceof FuncHandler) {
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

        blockHandler.addDef(def, fw.toString());

    }
}
