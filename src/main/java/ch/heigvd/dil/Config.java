package ch.heigvd.dil;

import java.io.File;
import java.io.FileNotFoundException;

public class Config {
  private String title;
  private String site_desc;
  private String domain;
  private String ssh_hostname;
  private String ssh_username;
  private String ssh_password;
  private String ssh_distpath;

  public Config() {
    title = "";
    site_desc = "";
    domain = "";
    ssh_hostname = "";
    ssh_username = "";
    ssh_password = "";
    ssh_distpath = "";
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
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

  public String getSsh_hostname() {
    return ssh_hostname;
  }

  public String getSsh_username() {
    return ssh_username;
  }

  public String getSsh_password() {
    return ssh_password;
  }

  public String getSsh_distpath() {
    return ssh_distpath;
  }

  public void setSsh_hostname(String ssh_hostname) {
    this.ssh_hostname = ssh_hostname;
  }

  public void setSsh_username(String ssh_username) {
    this.ssh_username = ssh_username;
  }

  public void setSsh_password(String ssh_password) {
    this.ssh_password = ssh_password;
  }

  public void setSsh_distpath(String ssh_distpath) {
    this.ssh_distpath = ssh_distpath;
  }

  public void writeConfigFile(File path) throws FileNotFoundException {
    Utils.writeYamlFile(this, path, null);
  }
}
