import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PbCompile {

  public static void main(String[] args) {
    // Relative path to the project directory
    String projectDirectory = ".";

    // Convert relative path to absolute path
    Path absolutePath = Paths.get(projectDirectory).toAbsolutePath();
    String absoluteProjectDirectory = absolutePath.toString();

    // Relative path to the directory containing .proto files
    String protoDirectory = Paths.get(absoluteProjectDirectory, "proto").toString();

    // Relative path to the directory where generated files will be saved
    String outputDirectory = Paths.get(absoluteProjectDirectory, "generated").toString();

    // Check if directories exist and are readable
    if (!checkDirectory(protoDirectory) || !checkDirectory(outputDirectory)) {
      System.out.println("Error: Unable to access directories.");
      return;
    }

    // Perform the compilation
    compileProtobufs(protoDirectory, outputDirectory);
  }

  private static boolean checkDirectory(String directory) {
    File dir = new File(directory);
    return dir.exists() && dir.isDirectory() && dir.canRead();
  }

  private static void compileProtobufs(String protoDirectory, String outputDirectory) {
    // Check if .proto files exist in proto directory
    File protoDir = new File(protoDirectory);
    File[] protoFiles = protoDir.listFiles((dir, name) -> name.endsWith(".proto"));

    if (protoFiles == null || protoFiles.length == 0) {
      System.out.println("Error: No .proto files found in " + protoDirectory);
      return;
    }

    // Implement your Protocol Buffer compilation logic here
    System.out.println("Proto directory: " + protoDirectory);
    System.out.println("Output directory: " + outputDirectory);
  }
}
