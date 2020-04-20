module be.hogent.model {
    requires java.sql;
    requires log4j.api;
    requires log4j.core;
    requires itextpdf;
    exports be.hogent.model;
    exports be.hogent.model.dao;
    exports be.hogent.model.reports;
}