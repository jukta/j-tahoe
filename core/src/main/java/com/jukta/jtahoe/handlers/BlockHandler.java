package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.gen.GenContext;
import com.jukta.jtahoe.gen.JavaSourceFromString;
import com.jukta.jtahoe.definitions.BlockMeta;

import javax.tools.JavaFileObject;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by aleph on 18.02.2016.
 */
public class BlockHandler extends AbstractHandler {
    private String body = "";
    protected Map<String, String> defs = new HashMap<String, String>();

    public BlockHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    public void appendCode(String code) {
        body += code;
    }

    public void addElement(String element) {
        body += getVarName() + ".addElement(" + element + ");\n";
    }

    public void addDef(String name, String body) {
        if (defs.put(name, body) != null) {
            throw new RuntimeException("Duplicate def in block: " + getAttrs().get("name"));
        }
    }

    @Override
    public void end() {
        String name = getAttrs().get("name");
        String parent = getAttrs().get("parent");

        if (parent != null && defs.size() == 0) {
            DefHandler defHandler = new DefHandler(getGenContext(), "sv:def", getAttrs(), this) {
                @Override
                public String getVarName() {
                    return BlockHandler.this.getVarName();
                }
            };
            defHandler.setName(null);
            defHandler.setBody(body);
            defHandler.end();
        }

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

        String relPath = genContext.getRootDir().toURI().relativize(genContext.getCurrentFile().getParentUri()).getPath();
        meta.setRelPath(relPath);

        JavaFileObject file = new JavaSourceFromString(relPath + name, meta.toSource());
        genContext.getFiles().add(file);
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
