package net.lizhaoweb.spring.mybatis.actable.manager.system;


import net.lizhaoweb.spring.mybatis.actable.command.CreateTableParam;

import java.util.List;

/**
 * @author sunchenbin
 * @version 2016年6月23日 下午6:07:21
 */
public interface SysMysqlCreateTableManager {

    void createMysqlTable();

    List<CreateTableParam> getAllFields(Class<?> clas);
}
