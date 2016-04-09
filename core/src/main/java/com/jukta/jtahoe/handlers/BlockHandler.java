package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.GenContext;
import com.jukta.jtahoe.JavaSourceFromString;

import javax.tools.*;
import java.io.File;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by aleph on 18.02.2016.
 */
public class BlockHandler extends AbstractHandler {
    String body = "new JBody()";
    Map<String, String> defs = new HashMap<String, String>();

    public BlockHandler(GenContext genContext, String name, Map<String, String> attrs, AbstractHandler parent) {
        super(genContext, name, attrs, parent);
    }

    public void appendCode(String code) {

    }

    public void addElement(String element) {
        body += ".addElement(" + element + ")";
    }

    public void addDef(String name, String body) {
        defs.put(name, body);
    }

    @Override
    public void end() {
        try {
            String name = getAttrs().get("name");
            String parent = getAttrs().get("parent");
            StringWriter fw = new StringWriter();
            String pack = getCurPackage();
            fw.write("package " + pack + ";");
            fw.write("import com.jukta.jtahoe.Attrs;");
            fw.write("import com.jukta.jtahoe.jschema.*;");
            fw.write("public class " + name);
            if (parent == null) {
                fw.write(" extends com.jukta.jtahoe.Block {");
            } else {
                fw.write(" extends " + parent + " {");
            }
            for (Map.Entry<String, String> entry : defs.entrySet()) {
                fw.write(entry.getKey());
                fw.write(entry.getValue());
            }
            if (parent == null) {
                fw.write("public JElement body(final Attrs attrs) {");
                fw.write("return " + body + ";");
                fw.write("}");
            }

            fw.write("}");
            fw.close();


            System.out.println(fw.toString());
            String relPath = genContext.getRootDir().toURI().relativize(genContext.getCurrentFile().getParentFile().toURI()).getPath();
            JavaFileObject file = new JavaSourceFromString(relPath + name, fw.toString());
            genContext.getFiles().add(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, String> getDefs() {
        return defs;
    }
}
