package com.donno.nj.util;

import java.util.Map;

import static com.google.common.collect.ImmutableMap.builder;
import static com.google.common.collect.Maps.newHashMap;

public class ParamMapBuilder {

    public static Map paginationParams(Integer pageNo, Integer pageSize, String orderBy) {
        return  newHashMap(builder()
                .put("orderBy", orderBy)
                .put("pageNo", pageNo)
                .put("pageSize", pageSize)
                .put("limit", pageSize)
                .put("offset", pageNo > 0 ? (pageSize * (pageNo - 1)) : 0)
                .build());
    }

}
