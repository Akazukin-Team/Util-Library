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
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Objects;

public class FileUtils {
    public static final String FILE_IS_NOT_DIRECTORY = "file is not a directory.";
    public static final String FILE_IS_NOT_EXISTS = "file is not exists.";

    private static final DateTimeFormatter FILENAME_DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd_HH.mm.ss", Locale.ROOT);

    /**
     * Retrieves the application file as an absolute file. The returned file
     * represents the current working directory in which the application is executed.
     *
     * @return a {@link File} object representing the application file with an absolute path.
     */
    @NotNull
    public static File getApplicationFile() {
        return new File("").getAbsoluteFile();
    }

    /**
     * Creates a directory at the specified path if it does not already exist.
     * {@link #createDirectory(Path)} is called with the provided path.
     *
     * @param path the string representation of the directory path to be created.
     *             Must not be null or empty.
     * @throws IOException if an I/O error occurs during the creation of the directory.
     */
    public static void createDirectory(final String path) throws IOException {
        FileUtils.createDirectory(Paths.get(path));
    }

    /**
     * Creates a new directory at the specified path. If the directory already exists,
     * the method does nothing. If an I/O error occurs while creating the directory,
     * an exception is thrown.
     *
     * @param path the path where the directory should be created. Must not be null and
     *             should represent a valid file path.
     * @throws IOException if an I/O error occurs or the directory cannot be created.
     */
    public static void createDirectory(final Path path) throws IOException {
        try {
            Files.createDirectory(path);
        } catch (final FileAlreadyExistsException ignored) {
        } catch (final IOException e) {
            throw e;
        }
    }

    @Nullable
    public static String getResourceAsString(final File file) throws IOException {
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

    /**
     * Reads all bytes from the specified file and returns them as a byte array.
     * If the file does not exist or is null, the method returns null.
     * If an I/O error occurs, it throws an IOException.
     *
     * @param file the file to read bytes from; must not be null
     * @return a byte array containing the contents of the file, or null if the file is not found
     * @throws IOException if an I/O error occurs while reading the file
     */
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

    /**
     * Retrieves a file within a JAR using the given class loader and resource path.
     * If the resource is located, it will return a {@link File} representation of the resource.
     * If the resource cannot be found or an error occurs, the method returns null.
     *
     * @param classLoader the {@link ClassLoader} used to locate the resource. Must not be null.
     * @param path the resource path inside the JAR file. Must not be null or empty.
     * @return a {@link File} object representing the resource if found, or*/
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

    /**
     * Extracts the base file name from a given file name, excluding the file extension.
     * If the file name does not contain a period ('.'), the original file name is returned.
     *
     * @param fileName the full name of the file, including the extension. Must not be null.
     * @return the base file name without the extension, or the original file name if no extension exists.
     */
    @Deprecated
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
