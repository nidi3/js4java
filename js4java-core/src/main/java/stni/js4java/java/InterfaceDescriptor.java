package stni.js4java.java;

import java.io.*;

/**
 *
 */
public class InterfaceDescriptor {
    private final String name;
    private final String content;

    public InterfaceDescriptor(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public void write(File basedir, String encoding) throws IOException {
        basedir.mkdirs();
        File out = new File(basedir, name.replace('.', '/') + ".java");
        try (Writer writer = new OutputStreamWriter(new FileOutputStream(out), encoding)) {
            writer.write(content);
        }
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }
}
