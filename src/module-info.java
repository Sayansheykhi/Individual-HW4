/**
 * Module descriptor
 */

module TP3 {
	exports databasePart1;
	exports application;

	requires java.sql;
	requires javafx.base;
	requires javafx.controls;
	requires javafx.graphics;
	requires junit;
	requires org.junit.jupiter.api;
	
	opens application to javafx.graphics, javafx.fxml;
}