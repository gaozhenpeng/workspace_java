package org.at.cmware.persistent.dao;

import org.at.cmware.persistent.entity.Sequense;

public interface SequenseDAO extends GenericDAO<Sequense, String> {
	public Long nextval(String seq_name);

	public Long currval(String seq_name);
}
