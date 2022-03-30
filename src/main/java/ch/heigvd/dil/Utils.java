package ch.heigvd.dil;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class Utils {


    public static void writeYamlFile(Object o, File filePath) throws FileNotFoundException {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        options.setCanonical(false);
        options.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);

        Representer representer = new Representer();
        representer.addClassTag(o.getClass(), Tag.MAP);

        Yaml yaml = new Yaml(representer, options);
        PrintWriter writer = new PrintWriter(filePath);
        yaml.dump(o, writer);
    }
}
