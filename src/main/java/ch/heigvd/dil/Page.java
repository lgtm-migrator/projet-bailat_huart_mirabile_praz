package ch.heigvd.dil;

import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Path;

public class Page {

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
        Utils.writeYamlFile(this, this.filePath.toFile());
    }
}
