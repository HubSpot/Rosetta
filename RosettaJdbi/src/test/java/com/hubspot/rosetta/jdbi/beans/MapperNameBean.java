package com.hubspot.rosetta.jdbi.beans;

public class MapperNameBean {

    private NameBean binderName = new NameBean("value");
    private NameBean mapperName = new NameBean("value");

    public NameBean getBinderName() {
        return binderName;
    }

    public void setBinderName(NameBean binderName) {
        this.binderName = binderName;
    }

    public NameBean getMapperName() {
        return mapperName;
    }

    public void setMapperName(NameBean mapperName) {
        this.mapperName = mapperName;
    }
}
