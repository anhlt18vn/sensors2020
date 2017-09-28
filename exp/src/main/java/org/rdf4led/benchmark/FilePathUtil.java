package org.rdf4led.benchmark;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by Anh Le-Tuan
 * Email: anh.letuan@tu-berlin.de
 * <p>
 * Date: 09.08.19
 */
public class FilePathUtil {

    /**
     * Search for the parent folder that holds a given file.
     *
     * @param paths    List contains all the found paths
     * @param root     root -> reduce the search space
     * @param fileName the file name
     */
    private static void searchParentPath(List<String> paths, File root, String fileName) {
        if (root.isDirectory()) {
            for (File child : root.listFiles()) {
                searchParentPath(paths, child, fileName);
            }
        } else {
            if (root.getName().contains(fileName)) {
                paths.add(FilenameUtils.getFullPath(root.getAbsolutePath()));
            }
        }
    }


    public static List<String> searchParentPath(String root, String fileName) {
        List<String> arrayList = new ArrayList<>();
        File file = new File(root);
        searchParentPath(arrayList, file, fileName);
        return arrayList;
    }


    public static String getParentPath(File file) {
        return file.getParentFile().getName() + "/";
    }


    public static String getParentParentPath(File file) {
        return file.getParentFile().getParentFile().getParent() + "/";
    }
}
