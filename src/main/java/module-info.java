module una.ac.cr.kingdomfantasy {
    requires javafx.controls;
    requires javafx.fxml;

    opens una.ac.cr.kingdomfantasy to javafx.fxml;
    exports una.ac.cr.kingdomfantasy;
}
