module com.example.gestioneparcheggio {
    requires javafx.controls;
    requires javafx.fxml;
            
                            
    opens com.example.gestioneparcheggio to javafx.fxml;
    exports com.example.gestioneparcheggio;
}