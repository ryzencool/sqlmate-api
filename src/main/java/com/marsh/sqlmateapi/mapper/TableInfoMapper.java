package com.marsh.sqlmateapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marsh.sqlmateapi.domain.TableInfo;

public interface TableInfoMapper extends BaseMapper<TableInfo> {


    Integer insertReturnId(TableInfo tableInfo);
}
