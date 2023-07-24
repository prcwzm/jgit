package org.example.dto;

public enum FuncAssociationEnum {
    CALLER("CALLER","调用方"),
    CALLEE("CALLEE","被调用方");
    private final String association;
    private final String desc;
    FuncAssociationEnum(String association, String desc){
        this.association = association;
        this.desc = desc;
    }

    public String getAssociation() {
        return association;
    }

    public String getDesc() {
        return desc;
    }
}