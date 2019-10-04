package project.hsm.xml.dtdformat;

import com.sun.xml.dtdparser.*;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;
import org.xml.sax.*;

public class App {
    public static void main(String[] args) {
        try {
            String home = System.getProperty("user.home");
            Path path = Paths.get(home, "git", "dtd-format", "static",
                                  "example.dtd");
            InputSource in =
                    new InputSource(new FileInputStream(path.toFile()));
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DTDParser parser = new DTDParser();
            // parser.setDtdHandler(new DTDIndentHandler(out));
            parser.setDtdHandler(new DTDIndentHandler(System.out));
            parser.parse(in);
            System.out.println(
                    new String(out.toByteArray(), Charset.defaultCharset()));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
