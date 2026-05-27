module una.ac.cr.kingdomfantasy {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires jakarta.persistence;

    opens una.ac.cr.kingdomfantasy to javafx.fxml;
    exports una.ac.cr.kingdomfantasy;
}
