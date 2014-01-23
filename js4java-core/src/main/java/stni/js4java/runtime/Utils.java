package stni.js4java.runtime;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

/**
 *
 */
class Utils {
    private Utils() {
    }

    static String loadResource(String name) {
        try {
            final InputStream in = Utils.class.getResourceAsStream(name);
            byte[] buf = new byte[in.available()];
            in.read(buf);
            return new String(buf, 0, buf.length, Charset.forName("utf-8"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static String join(String... ss) {
        String res = "";
        boolean first = true;
        for (String s : ss) {
            if (first) {
                first = false;
            } else {
                res += ",";
            }
            res += "'" + s + "'";
        }
        return res;
    }
}
