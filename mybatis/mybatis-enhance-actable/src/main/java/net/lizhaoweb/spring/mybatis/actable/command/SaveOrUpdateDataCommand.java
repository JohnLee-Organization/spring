package net.lizhaoweb.spring.mybatis.actable.command;

import lombok.*;

import java.util.Map;

@NoArgsConstructor
@RequiredArgsConstructor
public class SaveOrUpdateDataCommand {

    @Setter
    @Getter
    private Long id;

    @Setter
    @Getter
    @NonNull
    private Map<Object, Map<Object, Object>> tableMap;

//    public SaveOrUpdateDataCommand(Map<Object, Map<Object, Object>> tableMap) {
//        this.tableMap = tableMap;
//    }

//    public Long getId() {
//        return id;
//    }
//
//    public void setId(Long id) {
//        this.id = id;
//    }

//    public Map<Object, Map<Object, Object>> getTableMap() {
//        return tableMap;
//    }
//
//    public void setTableMap(Map<Object, Map<Object, Object>> tableMap) {
//        this.tableMap = tableMap;
//    }

}


