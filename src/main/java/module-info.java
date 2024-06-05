module server {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.data.commons;
    requires spring.data.relational;
    requires spring.jdbc;
    requires java.sql;
    requires spring.boot;
    requires spring.boot.autoconfigure;
    requires spring.web;


    exports com.mp3server;
    exports com.mp3server.server;
    opens com.mp3server.Controllers to javafx.fxml;
    opens com.mp3server.server to spring.core, spring.beans, spring.context;
}
