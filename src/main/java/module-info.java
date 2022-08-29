module com.toocol.rich_text_report {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.apache.commons.lang3;
    requires org.fxmisc.richtext;
    requires org.fxmisc.flowless;

    opens com.toocol.rich_text_report to javafx.fxml;
    exports com.toocol.rich_text_report;
}