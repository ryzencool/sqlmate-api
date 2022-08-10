package com.marsh.sqlmateapi.service;

import com.marsh.sqlmateapi.mapper.FavoriteProjectMapper;
import com.marsh.sqlmateapi.mapper.param.QueryFavoriteProjectParam;
import com.marsh.sqlmateapi.mapper.result.FavoriteProjectDetail;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteProjectService {

    private final FavoriteProjectMapper favoriteProjectMapper;

    public FavoriteProjectService(FavoriteProjectMapper favoriteProjectMapper) {
        this.favoriteProjectMapper = favoriteProjectMapper;
    }

    public List<FavoriteProjectDetail> listProject(QueryFavoriteProjectParam req) {
        return favoriteProjectMapper.listFavoriteProject(req);
    }
}
