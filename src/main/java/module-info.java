module server {
    requires javafx.fxml;
    requires javafx.controls;
    requires javafx.graphics;
    requires javafx.media;
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
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires jlayer;

    exports com.mp3server;
    exports com.mp3server.server;
    opens com.mp3server.Controllers to javafx.fxml, com.fasterxml.jackson.databind;
    opens com.mp3server.server to spring.core, spring.beans, spring.context, com.fasterxml.jackson.databind;
}
