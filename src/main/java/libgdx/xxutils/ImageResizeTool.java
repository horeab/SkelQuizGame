package libgdx.xxutils;

import org.imgscalr.Scalr;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

import libgdx.campaign.QuestionCategory;
import libgdx.campaign.QuestionDifficulty;
import libgdx.implementations.history.HistoryCategoryEnum;
import libgdx.implementations.history.HistoryDifficultyLevel;

class ImageResizeTool {

    static final String MACBOOK_ROOT_PATH = "/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources";
    static int MAX_WIDTH = 1000;
    static int MAX_HEIGHT = 1000;

    public static void main(String[] args) throws IOException {
        //root path should contain /tournament_resources/..../image_folder/
        //it should contain a temp folder in the image folder
        String rootPath = "/tournament_resources/implementations/quizgame/questions/images/maps/";

//        List<QuestionCategory> toIgnore = Arrays.asList(HistoryCategoryEnum.cat0, HistoryCategoryEnum.cat1);
//        for (QuestionDifficulty diff : HistoryDifficultyLevel.values()) {
//            for (HistoryCategoryEnum category : HistoryCategoryEnum.values()) {
//                if (toIgnore.contains(category)) {
//                    continue;
//                }
//                String pathname = rootPath + "diff" + diff.getIndex() + "/cat" + category.getIndex();
        String pathname = rootPath;
        deleteTemp(pathname);
        new File(MACBOOK_ROOT_PATH + pathname + "/temp").mkdirs();
        File folder = new File("/Users/macbook/IdeaProjects/SkelQuizGame/src/main/resources" + pathname);
        List<File> listOfFiles = Arrays.stream(Objects.requireNonNull(folder.listFiles()))
                .filter(e -> e.getName().contains(".jpg")
                        || e.getName().contains(".jpeg")
                        || e.getName().contains(".png"))
                .collect(Collectors.toList());

        for (File img : listOfFiles) {
            resize(img.getName(), pathname);
        }

//            }
//        }
    }

    private static void deleteTemp(String pathname) {
        File folder = new File(MACBOOK_ROOT_PATH + pathname + "/temp");
        Arrays.stream(Objects.requireNonNull(folder.listFiles())).forEach(File::delete);
        folder.delete();
    }

    private static void resize(String imgName, String pathName) throws IOException {
        ImageLoadSaveService imageLoadSaveService = new ImageLoadSaveService();
        BufferedImage image = imageLoadSaveService.load(pathName + "/" + imgName);
        if (image == null) {
            throw new RuntimeException("no image found for name " + pathName + "" + imgName);
        }
        BufferedImage resizedImage = Scalr.resize(image, Scalr.Mode.FIT_EXACT, getWidth(image), getHeight(image));

        String ext = "jpg";
//        String ext = "jpeg";
        imageLoadSaveService.save(resizedImage, pathName + "/temp/" + imgName.split("\\.")[0] + "." + ext, ext);
    }

    static int getWidth(BufferedImage image) {
        if (image.getHeight() > image.getWidth()) {
            return Math.round(getNewWidthForNewHeight(MAX_HEIGHT, image));
        } else {
            return MAX_WIDTH;
        }
    }

    static int getHeight(BufferedImage image) {
        if (image.getHeight() > image.getWidth()) {
            return MAX_HEIGHT;
        } else {
            return Math.round(getNewHeightForNewWidth(MAX_WIDTH, image));
        }
    }

    static float getNewWidthForNewHeight(float newHeight, float originalWidth, float originalHeight) {
        return (originalWidth / originalHeight) * newHeight;
    }

    static float getNewWidthForNewHeight(float newHeight, BufferedImage image) {
        return getNewWidthForNewHeight(newHeight, image.getWidth(), image.getHeight());
    }


    static float getNewHeightForNewWidth(float newWidth, float originalWidth, float originalHeight) {
        return (originalHeight / originalWidth) * newWidth;
    }

    static float getNewHeightForNewWidth(float newWidth, BufferedImage image) {
        return getNewHeightForNewWidth(newWidth, image.getWidth(), image.getHeight());
    }

    static class ImageLoadSaveService {


        BufferedImage load(String name) throws IOException {
            BufferedImage i = null;

            try {
                i = ImageIO.read(ImageLoadSaveService.class.getResourceAsStream(name));
            } catch (Exception ignored) {
                throw ignored;
            }

            return i;
        }

        void save(BufferedImage image, String name, String ext) throws IOException {
            System.out.println("resized " + name);
            try {
                BufferedImage newImage = new BufferedImage( image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
                newImage.createGraphics().drawImage( image, 0, 0, Color.BLACK, null);
                ImageIO.write(newImage, ext, new FileOutputStream(MACBOOK_ROOT_PATH + name));
            } catch (Exception ignored) {
                throw ignored;
            }
        }
    }

}


