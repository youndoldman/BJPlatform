package com.donno.nj.dao.base;

import com.donno.nj.aspect.OperationLog;

import java.util.List;
import java.util.Map;

public interface BaseDao<T> {

	T findById(Integer id);

	List<T> getList(Map m);

	Integer count(Map m);

	Long insert(T record);

	void delete(Integer id);

	void update(T object);



	void delete(Map params);

}
