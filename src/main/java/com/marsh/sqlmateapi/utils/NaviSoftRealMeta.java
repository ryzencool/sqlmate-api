package com.marsh.sqlmateapi.utils;

import java.io.Serializable;

public class NaviSoftRealMeta implements Serializable {

    private String Table;
    private String FieldName;
    private String AliasFieldName;

    public void setTable(String Table) {
        this.Table = Table;
    }

    public void setFieldName(String FieldName) {
        this.FieldName = FieldName;
    }

    public void setAliasFieldName(String AliasFieldName) {
        this.AliasFieldName = AliasFieldName;
    }

    public String getTable() {
        return Table;
    }

    public String getFieldName() {
        return FieldName;
    }

    public String getAliasFieldName() {
        return AliasFieldName;
    }

}