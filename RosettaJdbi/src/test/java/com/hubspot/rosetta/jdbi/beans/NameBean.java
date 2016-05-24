package com.hubspot.rosetta.jdbi.beans;

public class NameBean {

    public NameBean(String name) {

        this.name = name;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
