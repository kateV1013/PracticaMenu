module ni.edu.uam.practicamenu {
    requires javafx.controls;
    requires javafx.fxml;


    opens ni.edu.uam.practicamenu to javafx.fxml;
    exports ni.edu.uam.practicamenu;
}