package org.at.cmware.persistent.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.at.cmware.persistent.entity.CmDeposit;
import org.springframework.transaction.annotation.Transactional;

public class CmDepositDAOImpl extends GenericDAOImpl<CmDeposit, Long> implements CmDepositDAO {
	public CmDepositDAOImpl() {
		super();
	}

	@Override
	@Transactional(readOnly = true)
	public Integer findTotalAmountById(Long id) {
		String queryName = "QUERY.TOTAL_AMOUNT.FROM.CMDEPOSIT.BY_ID";
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("depositid", id);

		List sums = getJpaTemplate().findByNamedQueryAndNamedParams(queryName, params);
		if (sums != null) {
			return (Integer) sums.get(0);
		} else {
			return null;
		}
	}
}
