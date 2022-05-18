package libgdx.xxutils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.stream.Collectors;

class RenameFileTool {

    public static void main(String[] args) throws IOException {
        String pathname = "/Users/macbook/IdeaProjects/ImageScaler/src/main/resources/scr_standard";
        File folder = new File(pathname);
        new File(pathname + "/temp").mkdirs();
        List<File> listOfFiles = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(e -> e.getName().contains(".png"))
                .sorted(Comparator.comparing(e -> Integer.parseInt(e.getName().substring(2).replace(".png", ""))))
                .collect(Collectors.toList());
        for (int i = 0; i < listOfFiles.size(); i++) {
            if (listOfFiles.get(i).isFile()) {
                String fileName = listOfFiles.get(i).getName();
                int fileIndex = Integer.parseInt(fileName.substring(2).replace(".png", ""));
                System.out.println(fileName);
                File oldFile = new File(pathname + "/" + fileName);

                File newFile = new File(pathname + "/temp/" + fileName.substring(0, 2) + (fileIndex <= 1 ? fileIndex : (fileIndex + 3)) + ".png");

                InputStream is = null;
                OutputStream os = null;
                try {
                    is = new FileInputStream(oldFile);
                    os = new FileOutputStream(newFile);
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = is.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                } finally {
                    is.close();
                    os.close();
                }

            }
        }
    }
}
