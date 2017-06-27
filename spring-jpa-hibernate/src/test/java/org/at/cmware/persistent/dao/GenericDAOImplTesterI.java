package org.at.cmware.persistent.dao;

import java.util.List;

import org.at.cmware.persistent.entity.CmDeposit;

public interface GenericDAOImplTesterI extends GenericDAO<CmDeposit, Long> {
	public List<CmDeposit> findJPQL(String jpql);

	public List findSQL(String sql);

}
