package net.lizhaoweb.spring.mybatis.actable.command;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TableConfig {

    @Setter
    @Getter
    private List<String> primaryKey = new ArrayList<String>();

    @Setter
    @Getter
    private List<CreateTableParam> list = new ArrayList<CreateTableParam>();

    @Setter
    @Getter
    private Map<String, Object> map = new HashMap<String, Object>();

    public TableConfig(List<CreateTableParam> list, Map<String, Object> map) {
        if (list != null) {
            this.list = list;
            for (CreateTableParam tableColumn : list) {
                if (tableColumn.isFieldIsKey()) {
                    primaryKey.add(tableColumn.getFieldName());
                }
            }
        }
        if (map != null) {
            this.map = map;
        }
    }

    public TableConfig(List<CreateTableParam> list) {
        this(list, null);
//        if (list != null) {
//            this.list = list;
//        }
    }

    public TableConfig(Map<String, Object> map) {
        this(null, map);
//        this.map = map;
    }

//    public Map<String, Object> getMap() {
//        return map;
//    }
//
//    public void setMap(Map<String, Object> map) {
//        this.map = map;
//    }
//
//    public List<Object> getList() {
//        return list;
//    }
//
//    public void setList(List<Object> list) {
//        this.list = list;
//    }
}
