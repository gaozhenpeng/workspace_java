package org.at.cmware.persistent.dao;

import java.math.BigInteger;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.at.cmware.persistent.entity.Sequense;
import org.springframework.transaction.annotation.Transactional;

public class SequenseDAOImpl extends GenericDAOImpl<Sequense, String> implements SequenseDAO {

	@Override
	@Transactional
	public Long nextval(String seq_name) {
		EntityManager em = getJpaTemplate().getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Query q = em.createNativeQuery("{call nextval(:seq_name)}");
		q.setParameter("seq_name", seq_name);
		List r = q.getResultList();
		em.getTransaction().commit();
		BigInteger bi = (BigInteger) r.get(0);
		return bi.longValue();
	}

	@Override
	@Transactional
	public Long currval(String seq_name) {
		EntityManager em = getJpaTemplate().getEntityManagerFactory().createEntityManager();
		em.getTransaction().begin();
		Query q = em.createNativeQuery("select currval(:seq_name)");
		q.setParameter("seq_name", seq_name);
		List r = q.getResultList();
		em.getTransaction().commit();
		BigInteger bi = (BigInteger) r.get(0);
		return bi.longValue();
	}

}
