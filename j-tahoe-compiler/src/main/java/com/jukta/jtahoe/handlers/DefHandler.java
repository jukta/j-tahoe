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
        name = name == null ? getBlock(true).getBlockName() : name;
        if (name.contains(":")) {
            name = name.substring(name.lastIndexOf(":") + 1);
        }
        name = "def_" + name;
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
        BlockHandler blockHandler = getBlock(true);
        BlockHandler blockHandlerInt = getBlock(false);

        String defName = getDefName();

        StringWriter fw = new StringWriter();
        fw.write(".addDef(\"" + defName + "\", new BlockDef() {");

        if (blockHandler.equals(blockHandlerInt)) {
            fw.write("public JElement doDef(final Attrs attrs, Block block" + getVarName() + ")");
        } else {
            fw.write("public JElement doDef(final Attrs _attrs" + getVarName() + ", Block block" + getVarName() + ")");
        }

        fw.write("{");
        fw.write("JBody " + getVarName() + " = new JBody();\n");
        fw.write(body);
        fw.write("return " + getVarName() + ";");
        fw.write("}");
        fw.write("})");

        blockHandlerInt.addDef(defName, fw.toString());

        if (!defaultDef) {
            getParent().addElement("this.def(\"" + defName + "\", attrs)");
        }

    }
}
