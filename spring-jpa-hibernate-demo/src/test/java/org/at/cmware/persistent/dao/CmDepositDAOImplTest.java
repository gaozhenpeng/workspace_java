package org.at.cmware.persistent.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.at.cmware.SpringTestBase;
import org.at.cmware.persistent.entity.CmDeposit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CmDepositDAOImplTest extends SpringTestBase {
	private static final Logger logger = Logger.getLogger("org.at.cmware.test");
	private final CmDepositDAO cdDAO = (CmDepositDAO) ctx.getBean("cmDepositDAO");
	private final Date CUR_DATE = new Date();
	private final Long TERMINAL_ID = new Long(999);
	private final Long OPERATOR_ID = new Long(999);
	private final String TRACE_NUM_1 = "trace_num_1@test";
	private final String DEPOSIT_LABEL_1 = "deposit_lable_1@test";
	private final String TRACE_NUM_2 = "trace_num_2@test";
	private final String DEPOSIT_LABEL_2 = "deposit_lable_2@test";
	private CmDeposit cd1 = null;
	private CmDeposit cd2 = null;

	@Before
	public void setUp() {

		cd1 = new CmDeposit();

		cd1.setDepositLabel(DEPOSIT_LABEL_1);
		cd1.setTerminalid(TERMINAL_ID);
		cd1.setOperatorid(OPERATOR_ID);
		cd1.setDepositTime(CUR_DATE);
		cd1.setTraceNum(TRACE_NUM_1);
		cd1.setAutoAmount(123);
		cd1.setManualAmount(456);

		cd2 = new CmDeposit();

		cd2.setDepositLabel(DEPOSIT_LABEL_2);
		cd2.setTerminalid(TERMINAL_ID);
		cd2.setOperatorid(OPERATOR_ID);
		cd2.setDepositTime(CUR_DATE);
		cd2.setTraceNum(TRACE_NUM_2);

	}

	@After
	public void tearDown() {
		cd1 = null;
		cd2 = null;

		String sql = "delete from cm_deposit where instr(cm_deposit.DEPOSIT_LABEL, '@test') > 0;";
		int affectedLines = cdDAO.updateBySql(sql);
		logger.debug("Cleaned records: " + affectedLines);
	}

	@Test
	public void testFindAll() {
		List<CmDeposit> cds = (List<CmDeposit>) cdDAO.findAll();
		assertNotNull("No records.", cds);
		for (CmDeposit cd : cds) {
			assertNotNull("No id.", cd.getDepositid());
		}
	}

	@Test
	public void testSave() {

		CmDeposit cds1 = cdDAO.save(cd1);

		assertNotNull("No id after save.", cds1.getDepositid());
		logger.debug("deposit id after save:" + cds1.getDepositid());
		assertEquals("TerminalID not right", TERMINAL_ID, cds1.getTerminalid());
		assertEquals("OperatorID not right", OPERATOR_ID, cds1.getOperatorid());
		assertEquals("Current Date not right", CUR_DATE, cds1.getDepositTime());
		assertEquals("Tracenum not right", TRACE_NUM_1, cds1.getTraceNum());
	}

	@Test
	public void testUpdate() {

		CmDeposit cds1 = cdDAO.save(cd1);

		Long depID1 = cds1.getDepositid();
		logger.debug("deposit id after save:" + depID1);

		cd1.setDepositid(depID1);
		cd1.setTraceNum(TRACE_NUM_2);

		CmDeposit cds2 = cdDAO.update(cd1);
		Long depID2 = cds2.getDepositid();
		assertNotNull("No id after update.", depID2);
		logger.debug("deposit id after update:" + depID2);

		assertEquals("update should not change the id.", depID1, depID2);

		assertEquals("TerminalID not right", TERMINAL_ID, cds2.getTerminalid());
		assertEquals("OperatorID not right", OPERATOR_ID, cds2.getOperatorid());
		assertEquals("Current Date not right", CUR_DATE, cds2.getDepositTime());
		assertEquals("Tracenum not right", TRACE_NUM_2, cds2.getTraceNum());
	}

	@Test
	public void testUpdateBySQL() {
		cdDAO.save(cd1);

		String sql = "delete from cm_deposit where instr(cm_deposit.deposit_label, '@test') > 0;";
		int affectedLines = cdDAO.updateBySql(sql);
		assertEquals("Not only 1 record has been deleted.", 1, affectedLines);
	}

	@Test
	public void testDelete() {

		CmDeposit cds1 = cdDAO.save(cd1);
		Long depID1 = cds1.getDepositid();

		// only ID is required for deletion.
		CmDeposit onlyIDCD = new CmDeposit();
		onlyIDCD.setDepositid(depID1);

		cdDAO.delete(onlyIDCD);
	}

	@Test
	public void testFindById() {
		CmDeposit cds1 = cdDAO.save(cd1);
		Long depID1 = cds1.getDepositid();

		CmDeposit cds2 = cdDAO.findById(depID1);

		assertEquals("deposit id", cds1.getDepositid(), cds2.getDepositid());
		assertEquals("terminal id", cds1.getTerminalid(), cds2.getTerminalid());
		assertEquals("operator id", cds1.getOperatorid(), cds2.getOperatorid());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.000");
		assertEquals("date", sdf.format(cds1.getDepositTime()), sdf.format(cds2.getDepositTime()));
		assertEquals("trace number", cds1.getTraceNum(), cds2.getTraceNum());

	}

	@Test
	public void testFindRowCount() {
		int r1 = cdDAO.findRowCount();

		CmDeposit cds1 = cdDAO.save(cd1);

		int r2 = cdDAO.findRowCount();

		assertEquals("rowCount not +1.", r1 + 1, r2);
	}

	@Test
	public void testFindByNamedQueryAndNamedParams() {
		CmDeposit cds1 = cdDAO.save(cd1);
		Long depID1 = cds1.getDepositid();

		assertEquals("depositamount is expected to be 123", Integer.valueOf(123), cds1.getAutoAmount());
		assertEquals("enevlopamount is expected to be 456", Integer.valueOf(456), cds1.getManualAmount());

		Integer sum = cdDAO.findTotalAmountById(depID1);
		assertNotNull(sum);
		assertEquals("The result of sums is not expected", Integer.valueOf(123 + 456), sum);

	}
}
