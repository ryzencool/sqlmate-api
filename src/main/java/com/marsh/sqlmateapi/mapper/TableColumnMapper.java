package com.marsh.sqlmateapi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marsh.sqlmateapi.domain.TableColumn;
import com.marsh.sqlmateapi.mapper.result.TableDetailResult;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface TableColumnMapper extends BaseMapper<TableColumn> {

    List<TableDetailResult> listProjectColumns(@Param("projectId") Integer projectId);
}
