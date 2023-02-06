package ru.istu.labapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import net.coobird.thumbnailator.geometry.Positions;

import javax.swing.*;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;

public class MainController implements Initializable {
    @FXML
    ListView listView;
    @FXML
    ImageView imageView = new ImageView();
    @FXML
    TextField widthTextField, heightTextField, angleOfRotationTextField;
    @FXML
    RadioButton keepAspectRatioRadioButton, counterClockWiseRadioButton, clockWiseRadioButton, waterMarkRadioButton1, waterMarkRadioButton2, waterMarkRadioButton3,
                topLeftRadioButton, topCenterRadioButton, topRightRadioButton, centerLeftRadioButton, centerRadioButton, centerRightButton, bottomLeftRadioButton,
                bottomCenterRadioButton, bottomRightRadioButton, blackAndWhiteFilterRadoButton, invertFilterRadioButton, sepiaFilterRadioButton;
    @FXML
    ChoiceBox<String> formatChoiceBox, qualityJpgChoiceBox, degreeOfTransparencyChoiceBox;
    @FXML
    Text qualityJpgText, percentText;
    @FXML
    TabPane mainTabPain;

    private List<Image> listImages;
    private List<File> files;
    private final String[] formats = {"JPG", "PNG", "BMP"};
    private final String[] qualitiesJpg = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
    private final String[] degreesOfTransparency = {"10", "20", "30", "40", "50", "60", "70", "80", "90", "100"};
    private DataBase db;
    private int numFiles;
    private List<File> resultList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //saveImagesButton.setVisible(false);

        mainTabPain.setDisable(true);

        qualityJpgChoiceBox.getItems().addAll(qualitiesJpg);
        qualityJpgChoiceBox.getSelectionModel().select(7);

        degreeOfTransparencyChoiceBox.getItems().addAll(degreesOfTransparency);
        degreeOfTransparencyChoiceBox.getSelectionModel().select(4);

        formatChoiceBox.getItems().addAll(formats);
        formatChoiceBox.getSelectionModel().selectFirst();
        formatChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.equals("JPG")) {
                qualityJpgChoiceBox.setVisible(true);
                qualityJpgText.setVisible(true);
                percentText.setVisible(true);
            }
            else {
                qualityJpgChoiceBox.setVisible(false);
                qualityJpgText.setVisible(false);
                percentText.setVisible(false);
            }
        });

        ToggleGroup rotateToggleGroup = new ToggleGroup();
        counterClockWiseRadioButton.setToggleGroup(rotateToggleGroup);
        clockWiseRadioButton.setToggleGroup(rotateToggleGroup);

        ToggleGroup waterMarkToggleGroup = new ToggleGroup();
        waterMarkRadioButton1.setToggleGroup(waterMarkToggleGroup);
        waterMarkRadioButton2.setToggleGroup(waterMarkToggleGroup);
        waterMarkRadioButton3.setToggleGroup(waterMarkToggleGroup);

        ToggleGroup filtersToggleGroup = new ToggleGroup();
        blackAndWhiteFilterRadoButton.setToggleGroup(filtersToggleGroup);
        invertFilterRadioButton.setToggleGroup(filtersToggleGroup);
        sepiaFilterRadioButton.setToggleGroup(filtersToggleGroup);

        ToggleGroup positionsWaterMarkToggleGroup = new ToggleGroup();
        topLeftRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        topCenterRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        topRightRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        centerLeftRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        centerRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        centerRightButton.setToggleGroup(positionsWaterMarkToggleGroup);
        bottomLeftRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        bottomCenterRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);
        bottomRightRadioButton.setToggleGroup(positionsWaterMarkToggleGroup);

        angleOfRotationTextField.setTextFormatter(new TextFormatter<Integer>(change -> {
            // "^(?:36[0]|3[0-5][0-9]([.,][0-9])?|[12][0-9][0-9]([.,][0-9])?|[1-9]?[0-9]([.,][0-9])?)?$"
            if (!(change.getControlNewText().matches("^(?:36[0]|3[0-5][0-9]|[12][0-9][0-9]|[1-9]?[0-9])?$"))) return null;
            else return change;
        }));

        widthTextField.setTextFormatter(new TextFormatter<Integer>(change -> {
            if (!change.getControlNewText().matches("\\d{0,4}")) return null;
            else return change;
        }));

        heightTextField.setTextFormatter(new TextFormatter<Integer>(change -> {
            if (!change.getControlNewText().matches("\\d{0,4}")) return null;
            else return change;
        }));

        listView.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getClickCount() == 1) {
                if (listView.getItems() == null) return;
                Object selectedObj = listView.getSelectionModel().getSelectedItem();
                showPreviewImage(listView.getItems().indexOf(selectedObj));
            }
        });
    }

    public void showPreviewImage(int position) {
        Image img = listImages.get(position);
        imageView.setImage(img);
        imageView.setCache(true);
    }

    public void selectFilesButton() {
        listImages = new ArrayList<>();
        FileChooser fc = new FileChooser();
        fc.setTitle("Выбор изображений");
        fc.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));
        files = fc.showOpenMultipleDialog(null);

        if (files != null) {
            numFiles = 0;
            db = new DataBase();
            db.cleanDB();
            listView.getItems().clear();
            listImages.clear();
            for (File f: files) {
                db.add(f);
                listImages.add(new Image(f.toURI().toString()));
                listView.getItems().add(f.getName());
                numFiles++;
            }

            System.out.println("Объекты Image:");
            Stream stream = listImages.stream();
            stream.forEach(System.out::println);

            System.out.println("Полный путь к файлам:");
            stream = files.stream();
            stream.forEach(System.out::println);
        }
        imageView.setImage(null);
        mainTabPain.setDisable(false);
    }

    public void editSizeButton() {
        if (files != null) {
            if ((widthTextField != null && widthTextField.getText().trim().isEmpty()) || (heightTextField != null && heightTextField.getText().trim().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Не введены значения в полях \"Ширина\" и/или \"Высота\"!");
                return;
            } else {
                List<File> bufList = db.getAll(numFiles);

                resultList = ImageUtils.resizeImages(bufList, Integer.parseInt(widthTextField.getText()), Integer.parseInt(heightTextField.getText()), keepAspectRatioRadioButton.isSelected());

                db.updateDB(resultList, numFiles);
                JOptionPane.showMessageDialog(null, "Изменение размера изображений завершёно!");
                System.out.printf("Применено изменение размера изображений к списку изображений размера %d\n", bufList.size());
            }
        } else JOptionPane.showMessageDialog(null, "Не загружен ни один файл!");
    }

    public void editRotationButton() {
        if (files != null) {
            if ((angleOfRotationTextField != null && angleOfRotationTextField.getText().trim().isEmpty())) {
                JOptionPane.showMessageDialog(null, "Не введено значение в поле \"Угол\"!");
                return;
            } else {
                List<File> bufList = db.getAll(numFiles);
//                List<File> resultList;

                Thread rotateImgThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (clockWiseRadioButton.isSelected()) resultList = ImageUtils.rotateImages(bufList, Double.parseDouble(angleOfRotationTextField.getText()));
                        else resultList = ImageUtils.rotateImages(bufList, -Double.parseDouble(angleOfRotationTextField.getText()));
                    }
                });
                rotateImgThread.start();
                try {
                    rotateImgThread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                db.updateDB(resultList, numFiles);
//                JOptionPane.showMessageDialog(null, "Поворот изображений завершён!");
                System.out.printf("Применен поворот изображений к списку изображений размера %d\n", bufList.size());
            }
        } else JOptionPane.showMessageDialog(null, "Не загружен ни один файл!");
    }

    public void editFormatButton() {
        if (files != null) {
            List<File> bufList = db.getAll(numFiles);

            ImageUtils.editFormatImages(bufList, formatChoiceBox.getValue(), qualityJpgChoiceBox.getValue());

            JOptionPane.showMessageDialog(null, "Конвертация изображений завершёна!");
            System.out.printf("Применена конвертация изображений к списку изображений размера %d\n", bufList.size());
        }
        else JOptionPane.showMessageDialog(null, "Не загружен ни один файл!");
    }

    public void addWatermarkButton() {
        if (files != null) {
            List<File> bufList = db.getAll(numFiles);
//            List<File> resultList;
            String degreeOfTransparency = degreeOfTransparencyChoiceBox.getValue();
            Positions position;
            String pathToWatermark;

            if (waterMarkRadioButton1.isSelected()) pathToWatermark = "Image/WaterMark_Sample_1.jpg";
            else if (waterMarkRadioButton2.isSelected()) pathToWatermark = "Image/WaterMark_Sample_2.jpg";
            else if (waterMarkRadioButton3.isSelected()) pathToWatermark = "Image/WaterMark_Sample_3.jpg";
            else {
                JOptionPane.showMessageDialog(null, "Некорректный выбор водяного знака!");
                return;
            }

            if (topLeftRadioButton.isSelected()) position = Positions.TOP_LEFT;
            else if (topCenterRadioButton.isSelected()) position = Positions.TOP_CENTER;
            else if (topRightRadioButton.isSelected()) position = Positions.TOP_RIGHT;

            else if (centerLeftRadioButton.isSelected()) position = Positions.CENTER_LEFT;
            else if (centerRadioButton.isSelected()) position = Positions.CENTER;
            else if (centerRightButton.isSelected()) position = Positions.CENTER_RIGHT;

            else if (bottomLeftRadioButton.isSelected()) position = Positions.BOTTOM_LEFT;
            else if (bottomCenterRadioButton.isSelected()) position = Positions.BOTTOM_CENTER;
            else position = Positions.BOTTOM_RIGHT;

            resultList = ImageUtils.printWatermark(bufList, pathToWatermark, position, degreeOfTransparency);

            db.updateDB(resultList, numFiles);
            JOptionPane.showMessageDialog(null, "Водяной знак нанесён!");
            System.out.printf("Водяной знак нанесён на все изображения списка размера %s\n", bufList.size());
        } else JOptionPane.showMessageDialog(null, "Не загружен ни один файл!");
    }

    public void applyFilterButton() {
        List<File> bufList = db.getAll(numFiles);
//        List<File> resultList;

        if (blackAndWhiteFilterRadoButton.isSelected()) resultList = ImageUtils.blackAndWhiteFilter(bufList);
        else if (invertFilterRadioButton.isSelected()) resultList = ImageUtils.invertFilter(bufList);
        else if (sepiaFilterRadioButton.isSelected()) resultList = ImageUtils.sepiaFilter(bufList);
        else {
            JOptionPane.showMessageDialog(null, "Не выбран ни один фильтр!");
            return;
        }

        db.updateDB(resultList, numFiles);
        JOptionPane.showMessageDialog(null, "Фильтр применён!");
        System.out.printf("Фильтр применён к списку изображений размера %d!\n", files.size());
    }
}