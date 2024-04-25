package com.wennest.yeemo.vbadge.utils;

import com.wennest.yeemo.vbadge.VBadge;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    @SuppressWarnings("UnusedReturnValue")
    public static boolean create(@NotNull File file) {
        if (file.exists()) {
            return false;
        }

        File parent = file.getParentFile();
        if (parent == null) {
            return false;
        }

        //noinspection ResultOfMethodCallIgnored
        parent.mkdirs();
        try {
            //noinspection ResultOfMethodCallIgnored
            file.createNewFile();
        } catch (IOException exception) {
            VBadge.getInstance().getSLF4JLogger().error("An error occurred while creating the file.", exception);
            return false;
        }
        return true;
    }

    public static void copy(@NotNull final InputStream inputStream, @NotNull File file) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            byte[] array = new byte[1024];
            int read;
            while ((read = inputStream.read(array)) > 0) {
                fileOutputStream.write(array, 0, read);
            }
            fileOutputStream.close();
            inputStream.close();
        } catch (IOException exception) {
            VBadge.getInstance().getSLF4JLogger().error("An error occurred while copying the file.", exception);
        }
    }

    public static void mkdir(@NotNull File file) {
        try {
            //noinspection ResultOfMethodCallIgnored
            file.mkdir();
        } catch (Exception exception) {
            VBadge.getInstance().getSLF4JLogger().error("An error occurred while creating the directory.", exception);
        }
    }

    @NotNull
    public static List<File> getFiles(@NotNull String path, boolean deep) {
        List<File> names = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return names;

        for (File file : listOfFiles) {
            if (file.isFile()) {
                names.add(file);
            } else if (file.isDirectory() && deep) {
                names.addAll(getFiles(file.getPath(), deep));
            }
        }
        return names;
    }

    @NotNull
    public static List<File> getFolders(@NotNull String path) {
        List<File> dirs = new ArrayList<>();

        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles == null) return dirs;

        for (File f : listOfFiles) {
            if (f.isDirectory()) {
                dirs.add(f);
            }
        }

        return dirs;
    }

    public static boolean deleteRecursive(@NotNull String path) {
        File dir = new File(path);
        return dir.exists() && deleteRecursive(dir);
    }

    public static boolean deleteRecursive(@NotNull File dir) {
        File[] inside = dir.listFiles();
        if (inside != null) {
            for (File file : inside) {
                deleteRecursive(file);
            }
        }
        return dir.delete();
    }
}
