package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.JavaSourceFromString;
import com.jukta.jtahoe.definitions.BlockMeta;
import com.jukta.jtahoe.gen.model.NamedNode;

import javax.tools.JavaFileObject;
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
    private DefHandler defHandler;

    public BlockHandler(GenContext genContext, NamedNode node, AbstractHandler parent) {
        super(genContext, node, parent);
        if (getAttrs().get("parent") != null) {
            NamedNode defNode = new NamedNode("", "def_", new HashMap<String, String>(), getNode());
            defHandler = new DefHandler(genContext, defNode, this);
        }

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
        if (defs.put(name, body) != null) {
            throw new RuntimeException("Duplicate def in block: " + getBlockName());
        }
    }

    @Override
    public void end() {
        String name = getBlockName();
        if (name == null) {
            throw new RuntimeException("Undefined block name in namespace \"" + getGenContext().getCurrentNamespace() + "\"");
        }
        String parent = getAttrs().get("parent");

        DefHandler dh = defHandler;
        defHandler = null;
        if (defs.size() == 0 && dh != null && dh.body.length() > 0) dh.end();

        BlockMeta meta = new BlockMeta(getSeq(), name, getAttrs());
        meta.setBody(body);
        if (parent != null) {
            meta.setParentName(processPrefix(parent));
        }
        if (getAttrs().get("dataHandler") != null) {
            meta.setDataHandler(getAttrs().get("dataHandler"));
        }
        meta.setArgs(createArgs(getAttrs()));

        meta.setDefs(defs);
        meta.setPack(getCurPackage());

        String relPath = getCurPackage();
        meta.setRelPath(relPath);

        JavaFileObject file = new JavaSourceFromString(relPath + "/" + name, meta.toSource());
        String pack = meta.getPack();
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
}
