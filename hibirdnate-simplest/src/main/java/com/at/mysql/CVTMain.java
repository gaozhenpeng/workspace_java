package com.at.mysql;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class CVTMain {
	public static void main(String[] args) throws Exception {
		VarcharText vt = null;

		Configuration config = new Configuration().configure();
		SessionFactory sessionFactory = config.buildSessionFactory();

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		vt = new VarcharText();
		vt.setLchar("12345");
		session.save(vt);
		tx.commit();
		session.flush();
		session.close();

		session = sessionFactory.openSession();
		tx = session.beginTransaction();
		Query q = session.createQuery("from VarcharText t order by t.id");
		List<VarcharText> l = q.list();
		if (l != null) {
			for (VarcharText v : l) {
				System.out.println(v.getId() + " : " + v.getLchar());
			}
		}
		tx.commit();
		session.flush();
		session.close();
	}
}
