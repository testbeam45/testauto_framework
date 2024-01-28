package com.dd.automation.utilities;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.List;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.LF;

public class FileUtils {
    private static final Logger LOGGER = LogManager.getLogger();

    private static String zipResultsSource;
    private static ArrayList<String> zipList;

    private FileUtils() {

    }

    public static boolean isExisting(File file) {
        boolean result = false;

        try {
            if (file != null) {
                result = file.exists();
                if (!result) {
                    LOGGER.debug(String.format("%s does not exist.", file));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean isExisting(String fullName) {
        return isExisting(new File(fullName));
    }

    public static boolean isDirectory(File file) {
        boolean result = false;

        try {
            if (isExisting(file)) {
                result = file.isDirectory();
                if (!result) {
                    LOGGER.debug(String.format("%s is not a directory.", file));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean isDirectory(String fullName) {
        return isDirectory(new File(fullName));
    }

    public static boolean isPathWritable(Path path) {
        boolean result = false;

        try {
            result = Files.isWritable(path);
            if (!result) {
                LOGGER.warn(String.format("%s is not writable.", path));
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean isPathWritable(String path) {
        return isPathWritable(Paths.get(path));
    }

    public static boolean isLocked(String fullName) {
        boolean result = true;
        File file = new File(fullName);

        try {
            if (isExisting(file)) {
                result = !file.renameTo(file);
                if (!result) {
                    LOGGER.warn(String.format("%s is locked.", fullName));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean createFile(String fullName) {
        boolean result = false;
        File file = new File(fullName);

        try {
            String parentDir = file.getParent();
            new File(parentDir).mkdirs();
            if (isPathWritable(Paths.get(parentDir)) && !isExisting(file)) {
                result = file.createNewFile();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean deleteFile(String fullName) {
        boolean result = false;
        File file = new File(fullName);

        try {
            if (!file.isDirectory()) {
                result = Files.deleteIfExists(file.toPath());
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean deleteFiles(File file) {
        boolean result = false;

        try {
            if (file.isDirectory()) {
                org.apache.commons.io.FileUtils.cleanDirectory(file);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean deleteFiles(String fullName) {
        return deleteFiles(new File(fullName));
    }

    public static boolean copyFile(String fullNameSrc, String fullNameDest) {
        boolean result = false;
        File file = new File(fullNameSrc);

        try {
            if (!file.isDirectory()) {
                Files.copy(Paths.get(fullNameSrc), Paths.get(fullNameDest), StandardCopyOption.REPLACE_EXISTING);
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean launchFile(String fullName) {
        boolean result = false;
        File file = new File(fullName);

        try {
            if (isExisting(file)) {
                Desktop.getDesktop().open(file);
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean launchLink(String link) {
        boolean result = false;

        try {
            Desktop.getDesktop().browse(new URI(link));
            result = true;
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean createDirs(File file) {
        boolean result = false;

        try {
            if (!isExisting(file)) {
                result = file.mkdirs();
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean createDirs(String fullName) {
        return createDirs(new File(fullName));
    }

    public static void deleteDirs(String fullName) {
        deleteDirs(new File(fullName));
    }

    public static void deleteDirs(File file) {
        try {
            org.apache.commons.io.FileUtils.deleteDirectory(file);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    public static boolean moveDirs(String fullNameSrc, String fullNameDest) {
        boolean result = false;

        try {
            if (isExisting(fullNameSrc)) {
                List<File> srcFiles = getAllFiles(fullNameSrc);
                File destDir = new File(fullNameDest);

                for (File file : srcFiles) {
                    org.apache.commons.io.FileUtils.moveToDirectory(file, destDir, true);
                }
                result = true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean zipDir(String fullNameSrc, String fullNameDest) {
        fullNameDest = fullNameDest + ".zip";
        zipResultsSource = fullNameSrc;
        byte[] buffer = new byte[1024];
        boolean result = false;

        try {
            if (isExisting(fullNameSrc) && isDirectory(fullNameSrc)) {
                zipList = new ArrayList<>();
                createZipList(new File(zipResultsSource));
                try (FileOutputStream fos = new FileOutputStream(fullNameDest)) {
                    ZipOutputStream zos = new ZipOutputStream(fos);

                    for (String file : zipList) {
                        ZipEntry ze = new ZipEntry(file);
                        zos.putNextEntry(ze);

                        try (FileInputStream in = new FileInputStream(fullNameSrc + File.separator + file)) {
                            int len;
                            while ((len = in.read(buffer)) > 0) {
                                zos.write(buffer, 0, len);
                            }
                            in.close();
                        }
                    }
                    zos.closeEntry();
                    zos.close();
                    fos.flush();
                    fos.close();
                    result = isExisting(fullNameDest);
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean writeContent(String fullName, List<String> content) {
        boolean result = false;
        Path path = Paths.get(fullName);

        try {
            createFile(fullName);
            if (isExisting(fullName)) {
                try (BufferedWriter writer = Files.newBufferedWriter(path)) {
                    for (String line : content) {
                        writer.write(line + System.lineSeparator());
                    }
                    result = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static boolean writeContentAppend(String fullName, String content) {
        boolean result = false;
        Path path = Paths.get(fullName);

        try {
            createFile(fullName);
            if (isExisting(fullName)) {
                try (BufferedWriter writer = Files.newBufferedWriter(path, StandardOpenOption.APPEND)) {
                    writer.write(content + System.lineSeparator());
                    result = true;
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    public static String getFileContent(String fullName) {
        StringBuilder content = new StringBuilder(EMPTY);

        try {
            if (isExisting(fullName)) {
                try (BufferedReader reader = new BufferedReader(new FileReader(fullName))) {
                    reader.lines().forEach(line -> content.append(line).append(LF));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return content.toString();
    }

    public static List<String> getTextFileContentAsList(String fullName) {
        List<String> lines = new ArrayList<>();

        try {
            if (isExisting(fullName)) {
                if (fullName.toUpperCase(Locale.ENGLISH).endsWith(".TXT")) {
                    try (BufferedReader reader = new BufferedReader(new FileReader(fullName))) {
                        String line;
                        while ((line = reader.readLine()) != null) {
                            lines.add(line);
                        }
                    }
                } else {
                    LOGGER.warn(String.format("%s is not a text file.", fullName));
                }
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return lines;
    }

    public static String getLastModified(String fullName) {
        final AtomicReference<String> lastModified = new AtomicReference<>(EMPTY);
        File file = new File(fullName);

        Optional.ofNullable(file.listFiles()).ifPresent(files -> {
            try {
                if (isExisting(fullName) && isDirectory(fullName) && files.length > 0) {
                    lastModified.set(Arrays.asList(files).stream().max(Comparator.comparing(File::lastModified)).get().getPath());
                }
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        });

        return lastModified.get();
    }

    public static List<File> getAllFiles(String fullName) {
        List<File> files = new ArrayList<>();

        if (isDirectory(fullName)) {
            Path path = Paths.get(fullName);

            try (Stream<Path> paths = Files.walk(Paths.get(fullName))) {
                paths.forEach(filePath -> {
                    if (!isSameFile(filePath, path) && filePath.getParent().equals(path)) {
                        files.add(filePath.toFile());
                    }
                });
            } catch (Exception e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return files;
    }

    public static File getFile(String fullName) {
        if (isExisting(fullName))
            return new File(fullName);
        else
            return null;
    }

    public static FileInputStream getStreamFromFile(String fullName) {
        return getStreamFromFile(new File(fullName));
    }

    public static FileInputStream getStreamFromFile(File fullName) {
        try {
            return new FileInputStream(fullName);
        } catch (FileNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            return null;
        }
    }

    private static boolean isSameFile(Path path1, Path path2) {
        boolean result = false;

        try {
            result = Files.isSameFile(path1, path2);
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return result;
    }

    private static void createZipList(File fullNameSrc) {
        if (fullNameSrc.isFile()) {
            zipList.add(createZipEntry(fullNameSrc.getPath()));
        }

        if (fullNameSrc.isDirectory()) {
            String[] subNote = fullNameSrc.list();
            for (String filename : subNote) {
                createZipList(new File(fullNameSrc, filename));
            }
        }
    }

    private static String createZipEntry(String fullDirName) {
        return fullDirName.substring(zipResultsSource.length() + 1);
    }

    public static boolean watchDirectory(Path file) throws InterruptedException {
        Map<WatchKey, Path> keys;
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            keys = new HashMap<>();
            file.register(watcher, ENTRY_DELETE, ENTRY_MODIFY);
            while (true) {
                WatchKey key;
                key = watcher.take();
                key.pollEvents().forEach(watchEvent -> {
                    WatchEvent.Kind<?> kind = watchEvent.kind();
                    if (kind == ENTRY_MODIFY)
                        System.out.println("My source file has changed!!!");
                    if (kind == ENTRY_DELETE)
                        System.out.println("My source file has DELETED!!!");
                });
                boolean valid = key.reset();
                if (!valid) {
                    keys.remove(key);
                    if (keys.isEmpty()) break;
                }
            }
            watcher.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (final ClosedWatchServiceException e) {
        }
        return true;
    }
}
