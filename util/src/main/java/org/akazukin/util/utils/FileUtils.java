package org.akazukin.util.utils;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Objects;

public class FileUtils {
    public static final String FILE_IS_NOT_DIRECTORY = "file is not a directory.";
    public static final String FILE_IS_NOT_EXISTS = "file is not exists.";

    private static final DateTimeFormatter FILENAME_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);

    @NotNull
    public static File getApplicationFile() {
        return new File("").getAbsoluteFile();
    }

    public static void createDirectory(final String path) throws IOException {
        FileUtils.createDirectory(Paths.get(path));
    }

    public static void createDirectory(final Path path) throws IOException {
        try {
            Files.createDirectory(path);
        } catch (final FileAlreadyExistsException ignored) {
        } catch (final IOException e) {
            throw e;
        }
    }

    @Nullable
    public static String getContent(final File file) throws IOException {
        try {
            final byte[] res = getResource(file);
            if (res == null) {
                return null;
            }
            return new String(res);
        } catch (final IOException e) {
            throw e;
        }
    }

    @Nullable
    public static byte[] getResource(final File file) throws IOException {
        try (final FileInputStream fis = new FileInputStream(file)) {
            return IOUtils.readAllBytes(fis);
        } catch (final NullPointerException | FileNotFoundException ignored) {
            return null;
        } catch (final IOException e) {
            throw e;
        }
    }

    public static String getFilenameFormattedDateTime() {
        return FileUtils.FILENAME_DATE_TIME_FORMATTER.format(ZonedDateTime.now());
    }

    /**
     * @param classLoader The ClassLoader in which the resource
     * @param path        In source Jar
     * @return Bytes of the resources
     * @throws IOException throw from {@link java.io.InputStream#read(byte[])}
     */
    @Nullable
    public static byte[] getResourcesInJar(final ClassLoader classLoader, final String path) throws IOException {
        try (final InputStream is = classLoader.getResourceAsStream(path)) {
            if (is == null) {
                return null;
            }
            return IOUtils.readAllBytes(is);
        }
    }

    @Nullable
    public static File getPathInJar(final ClassLoader classLoader, final String path) {
        try {
            final Enumeration<URL> url = classLoader.getResources(path);
            if (url.hasMoreElements()) {
                return new File(url.nextElement().toURI());
            }
        } catch (final URISyntaxException | IOException ignored) {
        }
        return null;
    }

    public static String getBaseFileName(final String fileName) {
        final int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return fileName;
        }

        return fileName.substring(0, index);
    }

    /**
     * Recursively delete the specified folder and any files and directories inside it.
     *
     * @param folder the folder to delete
     * @throws FileNotFoundException folder not found
     */
    public static void deleteDirectory(final File folder) throws FileNotFoundException {
        if (!folder.exists()) {
            throw new FileNotFoundException();
        }
        if (folder.isFile()) {
            throw new IllegalStateException(FILE_IS_NOT_DIRECTORY);
        }

        for (final File f : Objects.requireNonNull(folder.listFiles())) {
            if (f.isFile()) {
                f.delete();
            } else {
                deleteDirectory(f);
            }
        }
    }
}
