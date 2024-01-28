import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class PbCompile {

  public static void main(String[] args) {
    // Get the current project directory
    String currentDirectory = System.getProperty("user.dir");

    // Moving to the input and output folders
    File currentDir = new File(currentDirectory);

    // Relative path to the directory containing .proto files
    File protoDirectory = currentDir.getParentFile();

    if (protoDirectory != null) {
      String protoFileDirectory = protoDirectory.getAbsolutePath();
      // Relative path to the directory where generated files will be saved
      File outputDirectory = protoDirectory.getParentFile().getParentFile();
      String outputFileDirectory = outputDirectory.getAbsolutePath() + File.separator + "java";

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
    // Get a list of .proto files in the directory
    File protoDir = new File(protoFileDirectory);
    File[] protoFiles = protoDir.listFiles((dir, name) -> name.endsWith(".proto"));

    if (protoFiles == null || protoFiles.length == 0) {
      System.out.println("Error: No .proto files found in " + protoFileDirectory);
      return;
    }

    // Compile .proto files in parallel
    Arrays.stream(protoFiles)
            .parallel()
            .forEach(protoFile -> compileProto(protoFile, protoFileDirectory, outputFileDirectory));
  }

  private static void compileProto(File protoFile, String protoFileDirectory, String outputFileDirectory) {
    String protoFilePath = protoFile.getAbsolutePath();
    String command = "protoc -I=" + protoFileDirectory + " --java_out=" + outputFileDirectory + " " + protoFilePath;

    try {
      Process process = new ProcessBuilder(command.split("\\s+"))
              .inheritIO() // Redirects standard output and error to the current Java process
              .start();

      // Wait for the process to finish
      int exitCode = process.waitFor();
      if (exitCode == 0) {
        System.out.println("Compilation of " + protoFilePath + " successful.");
      } else {
        System.out.println("Error: Compilation of " + protoFilePath + " failed with exit code " + exitCode);
      }
    } catch (IOException | InterruptedException e) {
      System.out.println("Error executing protoc command for " + protoFilePath + ": " + e.getMessage());
    }
  }
}