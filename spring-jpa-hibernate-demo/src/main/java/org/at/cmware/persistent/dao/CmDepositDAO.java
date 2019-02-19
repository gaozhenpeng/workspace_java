package org.at.cmware.persistent.dao;

import org.at.cmware.persistent.entity.CmDeposit;

public interface CmDepositDAO extends GenericDAO<CmDeposit, Long> {
	public Integer findTotalAmountById(Long id);
}
