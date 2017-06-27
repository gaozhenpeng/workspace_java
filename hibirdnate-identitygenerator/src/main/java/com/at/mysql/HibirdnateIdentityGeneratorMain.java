package com.at.mysql;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import com.at.mysql.vo.IdentityGenerator;

public class HibirdnateIdentityGeneratorMain {

	public static void main(String[] args) {
		IdentityGenerator ig = null;

		Configuration config = new Configuration().configure();
		SessionFactory sessionFactory = config.buildSessionFactory();

		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		ig = new IdentityGenerator();
		ig.setName("pos 1");
		System.out.println("after new");
		System.out.println("IG.id: " + ig.getId());
		System.out.println("IG.name: " + ig.getName());
		session.save(ig);
		ig.setName("pos 2");
		System.out.println("after session.save");
		System.out.println("IG.id: " + ig.getId());
		System.out.println("IG.name: " + ig.getName());
		tx.commit();
		ig.setName("pos 3");
		System.out.println("after tx.commit");
		System.out.println("IG.id: " + ig.getId());
		System.out.println("IG.name: " + ig.getName());

		session.flush();
		session.close();
		ig.setName("pos 4");
		System.out.println("after session.close");
		System.out.println("IG.id: " + ig.getId());
		System.out.println("IG.name: " + ig.getName());
	}
}
