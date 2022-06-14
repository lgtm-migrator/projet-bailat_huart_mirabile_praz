package ch.heigvd.dil.commands;

import ch.heigvd.dil.Config;
import ch.heigvd.dil.Utils;
import com.jcraft.jsch.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import picocli.CommandLine;
import picocli.CommandLine.Command;

@Command(name = "publish")
public class Publish extends SiteCommand {

  private Config config;

  @Override
  public Integer call() throws IOException, JSchException, SftpException {

    // Retrieves the config
    Path configPath = root.resolve(Utils.Paths.CONFIG_FILENAME).toAbsolutePath();
    config = Utils.parseYamlFile(configPath.toFile(), Config.class);

    // Build the site
    new CommandLine(new Build()).execute(root.toString());

    // Connects to the SSH server
    java.util.Properties config2 = new java.util.Properties();
    config2.put("StrictHostKeyChecking", "no");

    JSch jsch = new JSch();
    Session jschSession = jsch.getSession(config.getSsh_username(), config.getSsh_hostname());
    jschSession.setConfig(config2);
    jschSession.setPassword(config.getSsh_password());
    jschSession.connect();

    ChannelSftp channelSftp = (ChannelSftp) jschSession.openChannel("sftp");
    channelSftp.connect();

    // Local file path and destination path
    Path localFile = root.resolve(Utils.Paths.BUILD_FOLDER);
    Path remoteDir = Path.of(config.getSsh_distpath());

    recursiveFolderUpload(localFile, remoteDir, channelSftp);

    channelSftp.disconnect();
    jschSession.disconnect();

    System.out.println("Website published successfully !");

    return 0;
  }

  private static void recursiveFolderUpload(
      Path sourcePath, Path destinationPath, ChannelSftp channel)
      throws SftpException, IOException {

    channel.cd(destinationPath.toString());
    // Gets all contained files in sourcePath
    File sourceFile = sourcePath.toFile();
    File[] files = sourceFile.listFiles();

    // If source file is not empty
    if (files != null && !sourceFile.getName().startsWith(".")) {
      // We browse its items
      for (File f : files) {

        Path dstFilePath = destinationPath.resolve(f.getName());
        String dstFileName = dstFilePath.toString();

        // If it's a file we upload it
        if (f.isFile()) {
          System.out.println("Publishing file " + dstFileName);
          FileInputStream fis = new FileInputStream(f);
          channel.put(fis, dstFileName, ChannelSftp.OVERWRITE);
          fis.close();
          // Else it's a folder
        } else {

          // check if the folder is already existing, else create it
          try {
            channel.stat(dstFileName);
          } catch (Exception e) {
            channel.mkdir(f.getName());
          }

          // Then cd into it
          channel.cd(dstFileName);

          // And executes the function again in the newly created folder
          recursiveFolderUpload(f.toPath().toAbsolutePath(), dstFilePath, channel);
        }
      }
    }
  }
}
