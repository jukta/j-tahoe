package com.jukta.jtahoe.definitions;

import java.io.StringWriter;
import java.util.Map;

/**
 * Created by Dmitriy Dobrovolskiy on 06.07.2016.
 *
 * @since *.*.*
 */
public class BlockMeta {

    private int seq;
    private String pack;
    private String name;
    private BlockMeta parent;
    private String parentName;
    private String dataHandler;
    private String body;
    private String relPath;
    private Map<String, String> defs;
    private Map<String, String> attrs;
    private Map<String, String> args;

    public BlockMeta(int seq, String name, Map<String, String> attrs) {
        this.name = name;
        this.attrs = attrs;
        this.seq = seq;
    }

    public String toSource() {
        try {
            String dataHandler = attrs.get("dataHandler");
            StringWriter sw = new StringWriter();
            sw.write("package " + pack + ";");
            sw.write("import com.jukta.jtahoe.Attrs;");
            sw.write("import com.jukta.jtahoe.jschema.*;");
            sw.write("public class " + name);
            if (parentName == null) {
                sw.write(" extends com.jukta.jtahoe.Block {");
            } else {
                sw.write(" extends " + parentName + " {");
            }
            if (dataHandler != null) {
                sw.write("public " + name + "() {");
                sw.write("dataHandler = \"" + dataHandler + "\";");
                sw.write("}");
            }
            for (Map.Entry<String, String> entry : defs.entrySet()) {
                sw.write(entry.getKey());
                sw.write(entry.getValue());
            }
            if (parentName == null) {
                sw.write("public JElement body(final Attrs attrs) {");
                sw.write("super.body(attrs);\n");
                sw.write("callDataHandler(attrs);\n");
                sw.write("JBody " + getVarName() + " = new JBody();\n");

                sw.write(body);
                sw.write("return " + getVarName() + ";");
                sw.write("}");
            } else if (args != null && !args.isEmpty()) {
                sw.write("public void init(Attrs attrs) {");
                sw.write("super.init(attrs);");
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    sw.write("attrs.set(\"" + entry.getKey() + "\", " + entry.getValue() + ");");
                }
                sw.write("}");
            }

            sw.write("}");
            sw.close();
            String res = sw.toString();
            System.out.println(res);
            return res;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String getVarName() {
        return "__" + seq;
    }

    private String getFullName() {
        return pack + "." + name;
    }

    public void setPack(String pack) {
        this.pack = pack;
    }

    public void setParent(BlockMeta parent) {
        this.parent = parent;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public void setDataHandler(String dataHandler) {
        this.dataHandler = dataHandler;
    }

    public void setArgs(Map<String, String> args) {
        this.args = args;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setRelPath(String relPath) {
        this.relPath = relPath;
    }

    public void setDefs(Map<String, String> defs) {
        this.defs = defs;
    }

}
