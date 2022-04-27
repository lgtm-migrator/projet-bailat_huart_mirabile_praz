package ch.heigvd.dil;

import java.io.File;
import java.io.FileNotFoundException;

public class Config {
  private String site_title;
  private String site_desc;
  private String domain;
  private String site_path;

  public Config() {
    site_title = "";
    site_desc = "";
    domain = "";
    site_path = "";
  }

  public String getSiteTitle() {
    return site_title;
  }

  public void setSiteTitle(String site_title) {
    this.site_title = site_title;
  }

  public String getSiteDesc() {
    return site_desc;
  }

  public void setSiteDesc(String site_desc) {
    this.site_desc = site_desc;
  }

  public String getDomain() {
    return domain;
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public String getSitePath() {
    return site_path;
  }

  public void setSitePath(String site_path) {
    this.site_path = site_path;
  }

  public void writeConfigFile(File path) throws FileNotFoundException {
    Utils.writeYamlFile(this, path);
  }
}
