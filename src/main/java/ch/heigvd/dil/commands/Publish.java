package ch.heigvd.dil.commands;

import ch.heigvd.dil.Config;
import ch.heigvd.dil.Utils;
import com.jcraft.jsch.*;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

@Command(name = "publish")
public class Publish implements Callable<Integer> {

    private Config config;

  @Parameters(index = "0", description = "Path to the website folder")
  private Path path;

  @Override
  public Integer call() throws FileNotFoundException, JSchException, SftpException {

    // Retrieves the config
    Path configPath = path.resolve(Utils.Paths.CONFIG_FILENAME).toAbsolutePath();
    config = Utils.parseYamlFile(configPath.toFile(), Config.class);

    // Build the site
    System.out.println("Building website...");
    new CommandLine(new Build()).execute(path.toString());

    ChannelSftp channelSftp = setupJsch();
    channelSftp.connect();

    String localFile = config.getSitePath() + '/' + Utils.Paths.BUILD_FOLDER;
    String remoteDir = config.getSsh_distpath();

    recursiveFolderUpload(localFile, remoteDir, channelSftp);

    System.out.println("Website published successfully");

    return 0;
  }

    private ChannelSftp setupJsch() throws JSchException {

        java.util.Properties config2 = new java.util.Properties();
        config2.put("StrictHostKeyChecking", "no");

        JSch jsch = new JSch();
        Session jschSession = jsch.getSession(config.getSsh_username(), config.getSsh_hostname());
        jschSession.setConfig(config2);
        jschSession.setPassword(config.getSsh_password());
        jschSession.connect();

        return (ChannelSftp) jschSession.openChannel("sftp");
    }

    private static void recursiveFolderUpload(String sourcePath, String destinationPath, ChannelSftp channel) throws SftpException, FileNotFoundException {

        File sourceFile = new File(sourcePath);

        if (sourceFile.isFile()) {

            // copy if it is a file
            channel.cd(destinationPath);
            if (!sourceFile.getName().startsWith("."))
                channel.put(new FileInputStream(sourceFile), sourceFile.getName(), ChannelSftp.OVERWRITE);

        } else {

            System.out.println("inside else " + sourceFile.getAbsolutePath());
            File[] files = sourceFile.listFiles();

            if (files != null && !sourceFile.getName().startsWith(".")) {

                channel.cd(destinationPath);
                SftpATTRS attrs = null;

                // check if the directory is already existing
                try {
                    attrs = channel.stat(destinationPath + "/" + sourceFile.getName());
                } catch (Exception e) {
                    System.out.println(destinationPath + "/" + sourceFile.getName() + " not found");
                }

                // else create a directory
                if (attrs != null) {
                    System.out.println("Directory exists IsDir=" + attrs.isDir());
                } else {
                    System.out.println("Creating dir " + sourceFile.getName());
                    channel.mkdir(sourceFile.getName());
                }

                for (File f: files) {
                    recursiveFolderUpload(f.getAbsolutePath(), destinationPath + "/" + sourceFile.getName(), channel);
                }

            }
        }
    }
}
