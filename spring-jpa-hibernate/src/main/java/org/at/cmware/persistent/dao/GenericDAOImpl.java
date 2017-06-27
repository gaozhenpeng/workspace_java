package org.at.cmware.persistent.dao;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;

import org.springframework.orm.jpa.JpaCallback;
import org.springframework.orm.jpa.support.JpaDaoSupport;
import org.springframework.transaction.annotation.Transactional;

@SuppressWarnings("unchecked")
public class GenericDAOImpl<T, ID extends Serializable> extends JpaDaoSupport implements GenericDAO<T, ID> {

	private Class<T> persistentClass;

	public GenericDAOImpl() {
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass())
				.getActualTypeArguments()[0];
	}

	public Class<T> getPersistentClass() {
		return persistentClass;
	}

	@Override
	@Transactional
	public T save(Object entity) {
		return (T) getJpaTemplate().merge(entity);
	}

	@Override
	@Transactional
	public T update(Object entity) {
		return (T) getJpaTemplate().merge(entity);
	}

	@Override
	@Transactional
	public Integer updateBySql(final String sql) {
		return (Integer) getJpaTemplate().execute(new JpaCallback<Integer>() {
			public Integer doInJpa(EntityManager em) throws PersistenceException {
				return em.createNativeQuery(sql).executeUpdate();
			}
		});
	}

	@Override
	@Transactional
	public void delete(Object entity) {
		getJpaTemplate().remove(getJpaTemplate().merge(entity));
	}

	@Override
	@Transactional(readOnly = true)
	public T findById(Serializable id) {
		return (T) getJpaTemplate().find(this.getPersistentClass(), id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<T> findAll() {
		return getJpaTemplate().executeFind(new JpaCallback<List<T>>() {
			public List<T> doInJpa(EntityManager em) throws PersistenceException {
				StringBuffer jpql = new StringBuffer("from ");
				jpql.append(getPersistentClass().getName());
				jpql.append(" obj");
				return em.createQuery(jpql.toString()).getResultList();
			}
		});
	}

	@Override
	@Transactional(readOnly = true)
	public int findRowCount() {
		return ((Long) getJpaTemplate().execute(new JpaCallback<Long>() {
			public Long doInJpa(EntityManager em) throws PersistenceException {
				StringBuffer strBuff = new StringBuffer("select count(*) from ");
				strBuff.append(getPersistentClass().getName());
				return (Long) em.createQuery(strBuff.toString()).getResultList().get(0);
			}
		})).intValue();
	}

	@Override
	@Transactional(readOnly = true)
	public List findByNamedQueryAndNamedParams(String queryName, Map<String, ?> params) {
		return getJpaTemplate().findByNamedQueryAndNamedParams(queryName, params);
	}

	// 要立即抓取时用"JOIN FETCH"
	@Transactional(readOnly = true)
	protected List<T> findByJPQL(String jpql) {
		return getJpaTemplate().find(jpql);
	}

	/**
	 * 返回：list中装入的是对象数组(Object[])
	 */
	@Transactional(readOnly = true)
	protected List findBySQL(final String sql) {
		return getJpaTemplate().executeFind(new JpaCallback<List<T>>() {
			public List doInJpa(EntityManager em) throws PersistenceException {
				return em.createNativeQuery(sql).getResultList();
			}
		});
	}

}
