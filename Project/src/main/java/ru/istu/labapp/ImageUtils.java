package ru.istu.labapp;

import javafx.scene.image.Image;
import net.coobird.thumbnailator.Thumbnails;
import net.coobird.thumbnailator.geometry.Positions;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageUtils {
    public static List<File> resizeImages(List<File> files, int newWidth, int newHeight, boolean keepAspectRatio) {
        List<File> resultList = new ArrayList<>();
        for (File f : files) {
            try {
                Thumbnails.of(f)
                        .size(newWidth, newHeight)
                        .keepAspectRatio(keepAspectRatio)
                        .toFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultList.add(f);
        }

        return resultList;
    }

    public static List<File> rotateImages(List<File> files, double angle) {
        List<File> resultList = new ArrayList<>();

        for (File f : files) {
            Image image = new Image(f.toURI().toString());
            try {
                Thumbnails.of(f)
                        .size((int)(image.getWidth()), (int)(image.getHeight()))
                        .rotate(angle)
                        .toFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultList.add(f);
        }

        return resultList;
    }

    public static void editFormatImages(List<File> files, String format, String qualityJpg) {
        for (File f : files) {
            Image image = new Image(f.toURI().toString());
            try {
                if (format.equals("JPG")) {
                    Thumbnails.of(f)
                            .size((int) (image.getWidth()), (int) (image.getHeight()))
                            .outputFormat(format.toLowerCase())
                            .outputQuality(Float.parseFloat(qualityJpg) / 100.0)
                            .toFile(f);
                } else {
                    Thumbnails.of(f)
                            .size((int) (image.getWidth()), (int) (image.getHeight()))
                            .outputFormat(format.toLowerCase())
                            .toFile(f);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static List<File> printWatermark(List<File> files, String pathToWatermark, Positions position, String transparency) {
        List<File> resultList = new ArrayList<>();

        for (File f : files) {
            Image image = new Image(f.toURI().toString());
            try {
                Thumbnails.of(f)
                        .size((int) (image.getWidth()), (int) (image.getHeight()))
                        .watermark(position, ImageIO.read(MainController.class.getResource(pathToWatermark)), Float.parseFloat(transparency) / 100.0f)
                        .toFile(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
            resultList.add(f);
        }

        return resultList;
    }

    public static List<File> blackAndWhiteFilter(List<File> files) {
        List<File> resultList = new ArrayList<>();

        for (File f : files) {
            try {
                BufferedImage src = ImageIO.read(new File(f.getPath()));
                ColorConvertOp op = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);
                BufferedImage dest = op.filter(src, null);
                ImageIO.write(dest, f.getName().substring(1 + f.getName().lastIndexOf(".")).toLowerCase(), f);
                resultList.add(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    public static List<File> invertFilter(List<File> files) {
        List<File> resultList = new ArrayList<>();

        for (File f: files) {
            try {
                BufferedImage resultBI = ImageIO.read(new File(f.getPath()));
                for (int x = 0; x < resultBI.getWidth(); x++) {
                    for (int y = 0; y < resultBI.getHeight(); y++) {
                        int rgb = resultBI.getRGB(x, y);
                        Color color = new Color(rgb, true);
                        int r = 255 - color.getRed();
                        int g = 255 - color.getGreen();
                        int b = 255 - color.getBlue();
                        color = new Color(r, g, b, color.getAlpha());
                        resultBI.setRGB(x, y, color.getRGB());
                    }
                }
                ImageIO.write(resultBI, f.getName().substring(1 + f.getName().lastIndexOf(".")).toLowerCase(), f);
                resultList.add(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }

    public static List<File> sepiaFilter(List<File> files) {
        List<File> resultList = new ArrayList<>();

        for (File f: files) {
            try {
                BufferedImage resultBI = ImageIO.read(new File(f.getPath()));
                int width = resultBI.getWidth();
                int height = resultBI.getHeight();

                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        int p = resultBI.getRGB(x, y);

                        int a = (p >> 24) & 0xff;
                        int R = (p >> 16) & 0xff;
                        int G = (p >> 8) & 0xff;
                        int B = p & 0xff;

                        int newRed = (int)(0.393 * R + 0.769 * G + 0.189 * B);
                        int newGreen = (int)(0.349 * R + 0.686 * G + 0.168 * B);
                        int newBlue = (int)(0.272 * R + 0.534 * G + 0.131 * B);

                        if (newRed > 255) R = 255;
                        else R = newRed;
                        if (newGreen > 255) G = 255;
                        else G = newGreen;
                        if (newBlue > 255) B = 255;
                        else B = newBlue;

                        p = (a << 24) | (R << 16) | (G << 8) | B;
                        resultBI.setRGB(x, y, p);
                    }
                }
                ImageIO.write(resultBI, f.getName().substring(1 + f.getName().lastIndexOf(".")).toLowerCase(), f);
                resultList.add(f);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return resultList;
    }
}