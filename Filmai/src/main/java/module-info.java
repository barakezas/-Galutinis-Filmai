module com.example.filmai {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires org.hibernate.orm.core;
    requires java.persistence;
    requires java.naming;
    requires java.xml;
    requires java.sql;


    opens com.example.filmai to javafx.fxml;
    exports com.example.filmai;
    exports com.example.filmai.controller;
    opens com.example.filmai.controller to javafx.fxml;
    exports com.example.filmai.model;
    opens com.example.filmai.model to javafx.fxml;


}