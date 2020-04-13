module be.hogent.model {
    requires java.sql;
    requires log4j.api;
    requires log4j.core;
    exports be.hogent.model;
    exports be.hogent.model.dao;
}