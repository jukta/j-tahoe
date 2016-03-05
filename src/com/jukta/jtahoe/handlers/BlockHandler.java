package com.jukta.jtahoe.handlers;

import com.jukta.jtahoe.JavaSourceFromString;

import javax.tools.*;
import java.io.StringWriter;
import java.util.*;

/**
 * Created by aleph on 18.02.2016.
 */
public class BlockHandler extends AbstractHandler {
    String body = "new com.jukta.jtahoe.jschema.JBody()";
    Map<String, String> defs = new HashMap<String, String>();

    List<JavaFileObject> files;

    public BlockHandler(String name, Map<String, String> attrs, AbstractHandler parent) {
        super(name, attrs, parent);
    }

    public List<JavaFileObject> getFiles() {
        return files;
    }

    public void setFiles(List<JavaFileObject> files) {
        this.files = files;
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
                fw.write("public com.jukta.jtahoe.jschema.JElement body(final com.jukta.jtahoe.Attrs attrs) {");
                fw.write("return " + body + ";");
                fw.write("}");
            }

            fw.write("}");
            fw.close();


            System.out.println(fw.toString());
            JavaFileObject file = new JavaSourceFromString(name, fw.toString());
            files.add(file);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Map<String, String> getDefs() {
        return defs;
    }
}
