package org.at.cmware.persistent.dao;

import java.util.List;

import org.at.cmware.persistent.entity.CmDeposit;

public class GenericDAOImplTester extends GenericDAOImpl<CmDeposit, Long> implements GenericDAOImplTesterI {
	// Class Implementation start:
	public GenericDAOImplTester() {
		super();
	}

	public List<CmDeposit> findJPQL(String jpql) {
		return super.findByJPQL(jpql);
	}

	public List findSQL(String sql) {
		return super.findBySQL(sql);
	}
	// Class Implementation end;

}
