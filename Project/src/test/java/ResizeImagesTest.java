import org.junit.Test;
import ru.istu.labapp.ImageUtils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static junit.framework.TestCase.assertTrue;

public class ResizeImagesTest {
    @Test
    public void resizeImages() {
        List<File> actual = new ArrayList<>();
        List<File> expected = new ArrayList<>();
        int newWidth = 300, newHeight = 300;

        actual.add(new File("C:\\Users\\Kontrobaev Ruslan\\IdeaProjects\\LabApp\\src\\main\\resources\\ru\\istu\\labapp\\Image\\act.jpg"));
        expected.add(new File("C:\\Users\\Kontrobaev Ruslan\\IdeaProjects\\LabApp\\src\\main\\resources\\ru\\istu\\labapp\\Image\\exp.jpg"));

        actual = ImageUtils.resizeImages(actual, newWidth, newHeight, false);
        boolean isEqual = true;

        for (File file : actual) {
            for (File value : expected) {
                try {
                    BufferedImage actualImage = ImageIO.read(new File(file.getPath()));
                    BufferedImage expectedImage = ImageIO.read((new File(value.getPath())));
                    if (actualImage.getWidth() != expectedImage.getWidth() || actualImage.getHeight() != expectedImage.getHeight()) {
                        isEqual = false;
                        break;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        assertTrue(isEqual);
    }
}