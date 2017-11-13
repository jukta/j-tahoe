package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.JavaSourceFromString;
import com.jukta.jtahoe.gen.model.NamedNode;

import javax.tools.JavaFileObject;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class BlockHandler extends AbstractHandler {
    private String body = "";
    protected Map<String, String> defs = new HashMap<>();
    protected DefHandler defHandler;
    protected List<String[]> innerClasses = new ArrayList<>();

    public BlockHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
        if (getAttrs().get("parent") != null) {
            HashMap<String, String> attrs = new HashMap<>();
            attrs.put("name", getParentBlock());
            NamedNode defNode = new NamedNode("", "def_", attrs, getNode());
            defHandler = new DefHandler(genContext, defNode, this);
            defHandler.setDefaultDef(true);
        }

    }

    public void addInner(String blockName, String className, String s) {
        innerClasses.add(new String[] {blockName, className, s});
    }

    public void appendCode(String code) {
        if (defHandler != null) {
            defHandler.appendCode(code);
        } else {
            body += code;
        }
    }

    public void addElement(String element) {
        if (defHandler != null) {
            defHandler.addElement(element);
        } else {
            body += getVarName() + ".addElement(" + element + ");\n";
        }
    }

    public void addDef(String name, String body) {
//        defs.put(name, body);
        if (defs.put(name, body) != null) {
            throw new RuntimeException("Duplicate def in block: " + getBlockName());
        }
    }

    @Override
    public void end() {

        String name = getBlockName();
        String fullName = this.processPrefix(name);
        if (name == null) {
            throw new RuntimeException("Undefined block name in namespace \"" + getGenContext().getCurrentNamespace() + "\"");
        }
        Map<String, String> args = createArgs(getAttrs());

        String parentName = null;
        String parent = getAttrs().get("parent");
        if (parent != null) {
            parentName = processPrefix(parent);
        }

        String pack = getCurPackage();
        if (pack == null || "".equals(pack)) {
            throw new RuntimeException("Package is empty for block " + pack + "." + name);
        }

        DefHandler dh = defHandler;
        defHandler = null;
        if (dh != null && dh.body.length() > 0) dh.end();

        BlockHandler blockHandler = getBlock(true);

        try {
            String dataHandler = getAttrs().get("dataHandler");
            StringWriter sw = new StringWriter();
            if (blockHandler == null) {
                sw.write("package " + pack + ";");
                sw.write("import com.jukta.jtahoe.Attrs;");
                sw.write("import com.jukta.jtahoe.jschema.*;");
                sw.write("import com.jukta.jtahoe.Block;");
                sw.write("import com.jukta.jtahoe.BlockDef;");

                if (!innerClasses.isEmpty()) {
                    sw.write("import java.util.HashMap;");
                    sw.write("import java.util.Map;");
                }

                String buildId = genContext.getBuildId();
                if (buildId != null) {
                    sw.write("import com.jukta.jtahoe.BuildId;");
                    sw.write("@BuildId(\"" + buildId + "\")");
                }

                sw.write("public class " + name);
            } else {
                if (!name.contains(":")) {
                    fullName = pack + "." + name;
                }
                name = fullName.replaceAll("\\.", "_");
                sw.write("public static class " + name);
            }
            if (parentName == null) {
                sw.write(" extends Block {");
            } else {
                sw.write(" extends " + parentName + " {");
            }
            sw.write("Block" + " _" + name + " = this;");
            if (dataHandler != null || !innerClasses.isEmpty()) {
                sw.write("public " + name + "() {");

                if (dataHandler != null) {
                    sw.write("dataHandler = \"" + dataHandler + "\";");
                }

                if (!innerClasses.isEmpty()) {
                    for (String[] s : innerClasses) {
                        sw.write("inners.put(\"" + s[0] + "\", " + s[1] + ".class);");
                    }
                }

                sw.write("}\n");
            }


            if (!defs.isEmpty()) {
                sw.write("public void initDefs() {");
                sw.write("super.initDefs();");
                for (Map.Entry<String, String> entry : defs.entrySet()) {
//                sw.write(entry.getKey());

                    sw.write("this" + entry.getValue() + ";\n");
                }
                sw.write("}\n");
            }
            if (parentName == null) {
                sw.write("public void doBody(final JBody " + getVarName() + ", final Attrs attrs) {");
                sw.write("" + body + "\n");
                sw.write("}\n");
            } else if (args != null && !args.isEmpty()) {
                sw.write("public void init(Attrs attrs) {");
                sw.write("super.init(attrs);");
                for (Map.Entry<String, String> entry : args.entrySet()) {
                    sw.write("attrs.set(\"" + entry.getKey() + "\", " + entry.getValue() + ");");
                }
                sw.write("}\n");
            }

            if (!innerClasses.isEmpty()) {
                for (String[] s : innerClasses) {
                    sw.write(s[2]);
                }
            }

            sw.write("}\n");
            sw.close();
            String res = sw.toString();
            if (blockHandler == null) {
                if (System.getProperty("debug") != null) {
                    System.out.println(res);
                }

                String relPath = getCurPackage();

                JavaFileObject file = new JavaSourceFromString(relPath + "/" + name, res);

                GenContext.Package aPackage = genContext.getFiles().get(pack);
                if (aPackage == null) {
                    aPackage = new GenContext.Package(pack);
                    for (String n : genContext.getPrefixes().values()) {
                        if (!n.equals(genContext.getCurrentNamespace())) {
                            aPackage.getDependedPackages().add(getPackage(n));
                        }
                    }
                    genContext.getFiles().put(pack, aPackage);
                }
                aPackage.getJavaFileObjects().add(file);
            } else {
                blockHandler.addInner(fullName, name, res);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public String getBlockName() {
        return getAttrs().get("name");
    }

    private Map<String, String> createArgs(Map<String, String> attrs) {
        Map<String, String> args = new HashMap<>();
        for (Map.Entry<String, String> entry : attrs.entrySet()) {
            if ("name".equals(entry.getKey())
                    || "parent".equals(entry.getKey())
                    || "dataHandler".equals(entry.getKey())) {
                continue;
            }
            args.put(entry.getKey(), parseExp(entry.getValue(), true));
        }
        return args;
    }

    public Map<String, String> getDefs() {
        return defs;
    }

    public String getParentBlock() {
        String parent = getAttrs().get("parent");
        if (parent == null) return null;
            String[] p = parent.split(":");
            return p.length > 1 ? p[1] : p[0];
    }
}
