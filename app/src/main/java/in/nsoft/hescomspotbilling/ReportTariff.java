package in.nsoft.hescomspotbilling;

import java.math.BigDecimal;

public class ReportTariff {

	private String TariffCode;
	private String Units;
	private BigDecimal BillTotal;
	private String ConnectionNo;
	public String getTariffCode() {
		return TariffCode;
	}
	public void setTariffCode(String tariffCode) {
		TariffCode = tariffCode;
	}
	public String getUnits() {
		return Units;
	}
	public void setUnits(String units) {
		Units = units;
	}
	public BigDecimal getBillTotal() {
		return BillTotal;
	}
	public void setBillTotal(BigDecimal billTotal) {
		BillTotal = billTotal;
	}
	public String getConnectionNo() {
		return ConnectionNo;
	}
	public void setConnectionNo(String connectionNo) {
		ConnectionNo = connectionNo;
	}

}
