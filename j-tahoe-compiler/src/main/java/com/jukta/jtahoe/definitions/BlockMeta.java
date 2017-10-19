package com.jukta.jtahoe.definitions;

import com.jukta.jtahoe.gen.ArtifactInfo;
import com.jukta.jtahoe.gen.GenContext;

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
    private GenContext genContext;

    public BlockMeta(int seq, String name, Map<String, String> attrs, GenContext genContext) {
        this.name = name;
        this.attrs = attrs;
        this.seq = seq;
        this.genContext = genContext;
    }

    public String toSource() {
        if (pack == null || "".equals(pack)) {
            throw new RuntimeException("Package is empty for block " + getFullName());
        }
        try {
            String dataHandler = attrs.get("dataHandler");
            StringWriter sw = new StringWriter();
            sw.write("package " + pack + ";");
            sw.write("import com.jukta.jtahoe.Attrs;");
            sw.write("import com.jukta.jtahoe.jschema.*;");
            sw.write("import  com.jukta.jtahoe.Block;");

            ArtifactInfo artifactInfo = genContext.getArtifactInfo();
            if (artifactInfo != null) {
                sw.write("import com.jukta.jtahoe.ArtifactInfo;");
                sw.write("@ArtifactInfo(groupId = \"" + artifactInfo.getGroupId() + "\", artifactId = \"" + artifactInfo.getArtifactId() + "\", version = \"" + artifactInfo.getVersion() + "\")");
            }

            sw.write("public class " + name);
            if (parentName == null) {
                sw.write(" extends Block {");
            } else {
                sw.write(" extends " + parentName + " {");
            }
            sw.write("Block" + " _" + name + " = this;");
            if (dataHandler != null) {
                sw.write("public " + name + "() {");
                sw.write("dataHandler = \"" + dataHandler + "\";");
                sw.write("}\n");
            }
            for (Map.Entry<String, String> entry : defs.entrySet()) {
                sw.write(entry.getKey());
                sw.write(entry.getValue() + "\n");
            }
            if (parentName == null) {
                sw.write("public JElement body(final Attrs attrs) {");
                sw.write("super.body(attrs);\n");
                sw.write("final JBody " + getVarName() + " = new JBody();\n");

                sw.write("callDataHandler(attrs, new Block.Callback() {" +
                        "public void call() {" +
                        "if (attrs.getBlockHandler() != null) attrs.getBlockHandler().before(\""+pack+"." + name + "\", attrs, self());\n" +
                        "" + body + "\n" +
                        "if (attrs.getBlockHandler() != null) attrs.getBlockHandler().after(\""+pack+"." + name + "\", attrs, " + getVarName() + ", self());\n" +
                        "}" +
                        "});\n");

//                sw.write(body);
                sw.write("return " + getVarName() + ";");
                sw.write("}\n");
            } else if (args != null && !args.isEmpty()) {
                sw.write("public void init(Attrs attrs) {");
                sw.write("super.init(attrs);");
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    sw.write("attrs.set(\"" + entry.getKey() + "\", " + entry.getValue() + ");");
                }
                sw.write("}\n");
            }

            sw.write("}");
            sw.close();
            String res = sw.toString();
            if (System.getProperty("debug") != null) {
                System.out.println(res);
            }
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

    public String getPack() {
        return pack;
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
