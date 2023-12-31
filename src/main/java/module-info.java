module com.example.videoplayer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;

    opens com.example.videoplayer to javafx.fxml;
    exports com.example.videoplayer;
    exports com.example.videoplayer.Controller;
    opens com.example.videoplayer.Controller to javafx.fxml;
}