import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

public class PbCompile {

  public static void main(String[] args) {
    // Get the current project directory
    String currentDirectory = System.getProperty("user.dir");
    // System.out.println("Current Project Directory: " + currentDirectory);

    // Moving to the input and output folders
    File currentDir = new File(currentDirectory);

    // Relative path to the directory containing .proto files
    File protoDirectory = currentDir.getParentFile();

    if (protoDirectory != null) {
      String protoFileDirectory = protoDirectory.getAbsolutePath();
      // Relative path to the directory where generated files will be saved
      File outputDirectory = protoDirectory.getParentFile().getParentFile();
      String outputFileDirectory = Paths.get(outputDirectory.getAbsolutePath(), "java").toString();

      if (!checkDirectory(protoFileDirectory) || !checkDirectory(outputFileDirectory)) {
        System.out.println("Error: Unable to access directories.");
        return;
      }

      // Perform the compilation
      compileProtobufs(protoFileDirectory, outputFileDirectory);
    } else {
      System.out.println("Cannot move further back. Already at the root directory.");
    }
  }

  private static boolean checkDirectory(String directory) {
    File dir = new File(directory);
    return dir.exists() && dir.isDirectory() && dir.canRead();
  }

  private static void compileProtobufs(String protoFileDirectory, String outputFileDirectory) {
    // Use protoc compiler to compile .proto files
    // Define the command to execute
    String command =
        "protoc -I="
            + protoFileDirectory
            + " --java_out="
            + outputFileDirectory
            + " "
            + protoFileDirectory
            + "/"
            + "*.proto";

    try {
      // Execute the command
      Process process =
          new ProcessBuilder(command.split("\\s+"))
              .inheritIO() // Redirects standard output and error to the current Java process
              .start();

      // Wait for the process to finish
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        System.out.println("protoc command executed successfully.");
        System.out.println("protoc command  is *******: " + command);

      } else {
        System.out.println("Error: protoc command failed with exit code " + exitCode);
        System.out.println("protoc error command  is *******: " + command);
      }
    } catch (IOException | InterruptedException e) {
      System.out.println("Error executing protoc command: " + e.getMessage());
    }
  }
}
