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

  @Override
  public Integer call() throws IOException, JSchException, SftpException {

    // Parse config
    Path configPath = root.resolve(Utils.Paths.CONFIG_FILENAME).toAbsolutePath();
    Config config = Utils.parseYamlFile(configPath.toFile(), Config.class);

    // Build site
    new CommandLine(new Build()).execute(root.toString());

    // Connect to SSH server
    java.util.Properties sessionConfig = new java.util.Properties();
    sessionConfig.put("StrictHostKeyChecking", "no");

    JSch jsch = new JSch();
    Session jschSession = jsch.getSession(config.getSsh_username(), config.getSsh_hostname());
    jschSession.setConfig(sessionConfig);
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
    // Get all contained files in sourcePath
    File sourceFile = sourcePath.toFile();
    File[] files = sourceFile.listFiles();

    // If source file is not empty and not hidden file
    if (files != null && !sourceFile.getName().startsWith(".")) {
      for (File f : files) {
        Path dstFilePath = destinationPath.resolve(f.getName());
        String dstFileName = dstFilePath.toString();

        // If file upload directly
        if (f.isFile()) {
          System.out.println("Publishing file " + dstFileName);
          FileInputStream fis = new FileInputStream(f);
          channel.put(fis, dstFileName, ChannelSftp.OVERWRITE);
          fis.close();
        } else {
          // Else recursively upload folder

          try {
            channel.stat(dstFileName);
          } catch (Exception e) {
            channel.mkdir(f.getName());
          }

          channel.cd(dstFileName);

          recursiveFolderUpload(f.toPath().toAbsolutePath(), dstFilePath, channel);
        }
      }
    }
  }
}
