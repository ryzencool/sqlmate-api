package com.marsh.sqlmateapi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.marsh.sqlmateapi.domain.FavoriteProject;
import com.marsh.sqlmateapi.mapper.param.QueryFavoriteProjectParam;
import com.marsh.sqlmateapi.mapper.result.FavoriteProjectDetail;

import java.util.List;

public interface FavoriteProjectMapper extends BaseMapper<FavoriteProject> {

    List<FavoriteProjectDetail> listFavoriteProject(QueryFavoriteProjectParam param);
}
