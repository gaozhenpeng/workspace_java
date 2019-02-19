package org.at.cmware.persistent.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public interface GenericDAO<T, ID extends Serializable> {
	public T save(T entity);

	public T update(T entity);

	public Integer updateBySql(final String sql);

	public void delete(T entity);

	public T findById(ID id);

	public List<T> findAll();

	public int findRowCount();

	public List findByNamedQueryAndNamedParams(String queryName, Map<String, ?> params);
}
