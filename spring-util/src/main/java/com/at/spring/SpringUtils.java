package com.at.spring;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SpringUtils {
	Map<String, String> aMap = null;
	List<String> aList = null;
	Set<String> aSet = null;

	public Map<String, String> getaMap() {
		return aMap;
	}

	public void setaMap(final Map<String, String> aMap) {
		this.aMap = aMap;
	}

	public List<String> getaList() {
		return aList;
	}

	public void setaList(final List<String> aList) {
		this.aList = aList;
	}

	public Set<String> getaSet() {
		return aSet;
	}

	public void setaSet(final Set<String> aSet) {
		this.aSet = aSet;
	}

	public static void main(final String[] args) {
		final AbstractApplicationContext ctx = new ClassPathXmlApplicationContext(
				"classpath*:/applicationContext-resources.xml");
		final SpringUtils su = (SpringUtils) ctx.getBean("springUtils");
		for (final String lv : su.getaList()) {
			System.out.println("lv: " + lv);
		}
		final Map<String, String> m = su.getaMap();
		for (final String k : m.keySet()) {
			final String sv = m.get(k);
			System.out.println("mv: " + sv);
		}
		for (final String sv : su.getaSet()) {
			System.out.println("sv: " + sv);
		}
	}

}
