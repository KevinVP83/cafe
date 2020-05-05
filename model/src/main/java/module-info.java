module be.hogent.model {
    requires java.sql;
    requires log4j.api;
    requires itextpdf;
    requires jfreechart;
    exports be.hogent.model;
    exports be.hogent.model.dao;
    exports be.hogent.model.reports;
}