module com.empresa.hito_angelgallegofelipe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires mongo.java.driver;

    opens com.empresa.hito_angelgallegofelipe to javafx.fxml;
    exports com.empresa.hito_angelgallegofelipe;
}