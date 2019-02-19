package org.at.cmware.persistent.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.at.cmware.SpringTestBase;
import org.at.cmware.persistent.entity.CmDeposit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class GenericDAOImplTest extends SpringTestBase {
	// Class test start:
	private static final Logger logger = Logger.getLogger("org.at.cmware.test");

	private final GenericDAOImplTesterI gDAO = (GenericDAOImplTesterI) ctx.getBean("genericDAOImplTester");
	private final Date CUR_DATE = new Date();
	private final Long TERMINAL_ID = new Long(999);
	private final Long OPERATOR_ID = new Long(999);
	private final String TRACE_NUM_1 = "trace_num_1@test";
	private final String DEPOSIT_LABEL_1 = "deposit_lable_1@test";

	private CmDeposit cd1 = null;

	@Before
	public void setUp() {

		cd1 = new CmDeposit();

		cd1.setDepositLabel(DEPOSIT_LABEL_1);
		cd1.setTerminalid(TERMINAL_ID);
		cd1.setOperatorid(OPERATOR_ID);
		cd1.setDepositTime(CUR_DATE);
		cd1.setTraceNum(TRACE_NUM_1);
	}

	@After
	public void tearDown() {
		cd1 = null;

		String sql = "delete from cm_deposit where instr(cm_deposit.DEPOSIT_LABEL, '@test') > 0;";
		int affectedLines = gDAO.updateBySql(sql);
		logger.debug("Cleaned records: " + affectedLines);
	}

	@Test
	public void testFindByJPQL() {
		CmDeposit cds1 = gDAO.save(cd1);
		Long depID1 = cds1.getDepositid();

		String jpql = "select t from CmDeposit t where t.depositid = " + depID1;
		List<CmDeposit> cds = gDAO.findJPQL(jpql);

		assertNotNull("No results", cds);
		assertEquals("Not only 1 result", 1, cds.size());
		CmDeposit cd = cds.get(0);

		assertEquals("DepositID not right", depID1, cd.getDepositid());
		assertEquals("TerminalID not right", TERMINAL_ID, cd.getTerminalid());
		assertEquals("OperatorID not right", OPERATOR_ID, cd.getOperatorid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
		assertEquals("Current Date not right", sdf.format(CUR_DATE), sdf.format(cd.getDepositTime()));
		assertEquals("Tracenum not right", TRACE_NUM_1, cd.getTraceNum());
	}

	@Test
	public void testFindBySQL() {
		CmDeposit cds1 = gDAO.save(cd1);
		Long depID1 = cds1.getDepositid();

		String sql = "select t.terminalid from cm_deposit t where t.depositid = " + depID1;
		List cds = gDAO.findSQL(sql);
		assertNotNull("no result.", cds);
		assertEquals("Not only 1 result", 1, cds.size());

		BigInteger obj = (BigInteger) cds.get(0);
		assertEquals("terminal id not right", TERMINAL_ID.longValue(), Long.valueOf(obj.toString()).longValue());
	}
}
