package ch.heigvd.dil;

import org.yaml.snakeyaml.introspector.Property;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

public class Page {

  private static Representer representer;

  static {
    Representer representer =
        new Representer() {
          @Override
          protected Set<Property> getProperties(Class<?> type) {
            Set<Property> properties = super.getProperties(type);
            return properties.stream()
                .filter(v -> !v.getName().equals("filePath"))
                .collect(Collectors.toSet());
          }
        };
    representer.addClassTag(Page.class, Tag.MAP);
  }

  private Path filePath;
  private String title;
  private String author;
  private String date;

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getAuthor() {
    return author;
  }

  public void setAuthor(String author) {
    this.author = author;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public Path getFilePath() {
    return filePath;
  }

  public void setFilePath(Path filePath) {
    this.filePath = filePath;
  }

  public void initPageFile() throws FileNotFoundException {
    Utils.writeYamlFile(this, this.filePath.toFile(), representer);
  }

  public void appendContent(String content) throws IOException {
    Utils.appendToFile(content, this.filePath.toFile());
  }
}
