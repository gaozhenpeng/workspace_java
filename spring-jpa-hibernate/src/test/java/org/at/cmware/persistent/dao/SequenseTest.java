package org.at.cmware.persistent.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.at.cmware.SpringTestBase;
import org.at.cmware.persistent.entity.Sequense;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class SequenseTest extends SpringTestBase {
	private final SequenseDAO seqDAO = (SequenseDAO) ctx.getBean("sequenseDAO");

	private final String SEQ_NAME_1 = "seq_1";
	private final Long CURR_VALUE_1 = 1l;
	private final Boolean IS_CYCLE_1 = true;
	private final Integer INCREMENT_1 = 1;
	private final Long MAX_VALUE_1 = 3l;
	private final Integer MIN_VALUE_1 = 1;

	private final String SEQ_NAME_2 = "seq_2";

	private Sequense sq1 = null;
	private Sequense sq2 = null;

	private Sequense sqs1 = null;
	private Sequense sqs2 = null;

	@Before
	public void setUp() {
		sq1 = new Sequense();
		sq1.setSeqName(SEQ_NAME_1);
		sq1.setSeqCurrValue(CURR_VALUE_1);
		sq1.setSeqCycle(IS_CYCLE_1);
		sq1.setSeqIncrement(INCREMENT_1);
		sq1.setSeqMaxValue(MAX_VALUE_1);
		sq1.setSeqMinValue(MIN_VALUE_1);

		sq2 = new Sequense();
		sq2.setSeqName(SEQ_NAME_2);
	}

	@After
	public void tearDown() {
		if (sqs1 != null) {
			seqDAO.delete(sqs1);
		}

		if (sqs2 != null) {
			seqDAO.delete(sqs2);
		}
	}

	@Test
	public void testDummy() {
		assertTrue(true);
	}

	@Test
	public void testNextval_DEFAULT() {
		sqs2 = seqDAO.save(sq2);

		Long r1 = seqDAO.nextval(SEQ_NAME_2);
		assertNotNull("R1 is expected to be returned.", r1);
		Long r2 = seqDAO.nextval(SEQ_NAME_2);
		assertNotNull("R2 is expected to be returned.", r2);
		assertEquals("R2 is expected to increase 1", (r1 + 1), r2.longValue());
	}

	@Test
	public void testNextval_NEWSEQ() {
		sqs1 = seqDAO.save(sq1);

		Long r1 = seqDAO.nextval(SEQ_NAME_1);
		assertNotNull("R1 is expected to be returned.", r1);
		Long r2 = seqDAO.nextval(SEQ_NAME_1);
		assertNotNull("R2 is expected to be returned.", r2);
		assertEquals("R2 is expected to increase 1", (r1 + 1), r2.longValue());

		Long r3 = seqDAO.nextval(SEQ_NAME_1);
		assertNotNull("R3 is expected to be returned.", r3);
		assertEquals("R3 is expected to increase 1", (r2 + 1), r3.longValue());

		Long r4 = seqDAO.nextval(SEQ_NAME_1);
		assertNotNull("R3 is expected to be returned.", r4);
		assertEquals("R4 is expected to return 1", 1, r4.longValue());
	}

}
