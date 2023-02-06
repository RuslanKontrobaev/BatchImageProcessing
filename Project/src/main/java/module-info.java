module ru.istu.labapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires transitive javafx.base;
    requires transitive javafx.graphics;

    requires transitive java.desktop;

    requires com.dlsc.formsfx;
    requires validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires thumbnailator;
    requires java.sql;

    opens ru.istu.labapp to javafx.fxml;
    exports ru.istu.labapp;
}