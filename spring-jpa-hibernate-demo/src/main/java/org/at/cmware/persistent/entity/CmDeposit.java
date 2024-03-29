package org.at.cmware.persistent.entity;

// Generated 2011-9-27 13:20:46 by Hibernate Tools 3.4.0.CR1

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * CmDeposit generated by hbm2java
 */
@Entity
@Table(name = "cm_deposit", catalog = "cmware")
@NamedQueries({
    @NamedQuery(name = "QUERY.TOTAL_AMOUNT.FROM.CMDEPOSIT.BY_ID",
               query = "SELECT (t.autoAmount + t.manualAmount) as ta "
                     + "  FROM CmDeposit t "
                     + "  WHERE t.depositid = :depositid")
})

public class CmDeposit implements java.io.Serializable {

	private Long depositid;
	private String depositLabel;
	private String traceNum;
	private Long shiftid;
	private Integer batchid;
	private Long terminalid;
	private Long operatorid;
	private Integer autoAmount;
	private Integer count;
	private Integer manualAmount;
	private Date depositTime;
	private String rc1id;
	private String rc1info;
	private String rc2id;
	private String rc2info;
	private String rc3id;
	private String rc3info;
	private String rc4id;
	private String rc4info;
	private String detail;
	private String respCode;
	private String respMsg;

	public CmDeposit() {
	}

	public CmDeposit(String depositLabel, Date depositTime) {
		this.depositLabel = depositLabel;
		this.depositTime = depositTime;
	}

	public CmDeposit(String depositLabel, String traceNum, Long shiftid, Integer batchid, Long terminalid,
			Long operatorid, Integer autoAmount, Integer count, Integer manualAmount, Date depositTime, String rc1id,
			String rc1info, String rc2id, String rc2info, String rc3id, String rc3info, String rc4id, String rc4info,
			String detail, String respCode, String respMsg) {
		this.depositLabel = depositLabel;
		this.traceNum = traceNum;
		this.shiftid = shiftid;
		this.batchid = batchid;
		this.terminalid = terminalid;
		this.operatorid = operatorid;
		this.autoAmount = autoAmount;
		this.count = count;
		this.manualAmount = manualAmount;
		this.depositTime = depositTime;
		this.rc1id = rc1id;
		this.rc1info = rc1info;
		this.rc2id = rc2id;
		this.rc2info = rc2info;
		this.rc3id = rc3id;
		this.rc3info = rc3info;
		this.rc4id = rc4id;
		this.rc4info = rc4info;
		this.detail = detail;
		this.respCode = respCode;
		this.respMsg = respMsg;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "depositid", unique = true, nullable = false)
	public Long getDepositid() {
		return this.depositid;
	}

	public void setDepositid(Long depositid) {
		this.depositid = depositid;
	}

	@Column(name = "deposit_label", nullable = false, length = 20)
	public String getDepositLabel() {
		return this.depositLabel;
	}

	public void setDepositLabel(String depositLabel) {
		this.depositLabel = depositLabel;
	}

	@Column(name = "trace_num", length = 20)
	public String getTraceNum() {
		return this.traceNum;
	}

	public void setTraceNum(String traceNum) {
		this.traceNum = traceNum;
	}

	@Column(name = "shiftid")
	public Long getShiftid() {
		return this.shiftid;
	}

	public void setShiftid(Long shiftid) {
		this.shiftid = shiftid;
	}

	@Column(name = "batchid")
	public Integer getBatchid() {
		return this.batchid;
	}

	public void setBatchid(Integer batchid) {
		this.batchid = batchid;
	}

	@Column(name = "terminalid")
	public Long getTerminalid() {
		return this.terminalid;
	}

	public void setTerminalid(Long terminalid) {
		this.terminalid = terminalid;
	}

	@Column(name = "operatorid")
	public Long getOperatorid() {
		return this.operatorid;
	}

	public void setOperatorid(Long operatorid) {
		this.operatorid = operatorid;
	}

	@Column(name = "auto_amount")
	public Integer getAutoAmount() {
		return this.autoAmount;
	}

	public void setAutoAmount(Integer autoAmount) {
		this.autoAmount = autoAmount;
	}

	@Column(name = "count")
	public Integer getCount() {
		return this.count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}

	@Column(name = "manual_amount")
	public Integer getManualAmount() {
		return this.manualAmount;
	}

	public void setManualAmount(Integer manualAmount) {
		this.manualAmount = manualAmount;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "deposit_time", nullable = false, length = 19)
	public Date getDepositTime() {
		return this.depositTime;
	}

	public void setDepositTime(Date depositTime) {
		this.depositTime = depositTime;
	}

	@Column(name = "rc1id", length = 20)
	public String getRc1id() {
		return this.rc1id;
	}

	public void setRc1id(String rc1id) {
		this.rc1id = rc1id;
	}

	@Column(name = "rc1info", length = 64)
	public String getRc1info() {
		return this.rc1info;
	}

	public void setRc1info(String rc1info) {
		this.rc1info = rc1info;
	}

	@Column(name = "rc2id", length = 20)
	public String getRc2id() {
		return this.rc2id;
	}

	public void setRc2id(String rc2id) {
		this.rc2id = rc2id;
	}

	@Column(name = "rc2info", length = 64)
	public String getRc2info() {
		return this.rc2info;
	}

	public void setRc2info(String rc2info) {
		this.rc2info = rc2info;
	}

	@Column(name = "rc3id", length = 20)
	public String getRc3id() {
		return this.rc3id;
	}

	public void setRc3id(String rc3id) {
		this.rc3id = rc3id;
	}

	@Column(name = "rc3info", length = 64)
	public String getRc3info() {
		return this.rc3info;
	}

	public void setRc3info(String rc3info) {
		this.rc3info = rc3info;
	}

	@Column(name = "rc4id", length = 20)
	public String getRc4id() {
		return this.rc4id;
	}

	public void setRc4id(String rc4id) {
		this.rc4id = rc4id;
	}

	@Column(name = "rc4info", length = 64)
	public String getRc4info() {
		return this.rc4info;
	}

	public void setRc4info(String rc4info) {
		this.rc4info = rc4info;
	}

	@Column(name = "detail", length = 512)
	public String getDetail() {
		return this.detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	@Column(name = "resp_code", length = 10)
	public String getRespCode() {
		return this.respCode;
	}

	public void setRespCode(String respCode) {
		this.respCode = respCode;
	}

	@Column(name = "resp_msg", length = 256)
	public String getRespMsg() {
		return this.respMsg;
	}

	public void setRespMsg(String respMsg) {
		this.respMsg = respMsg;
	}

}
