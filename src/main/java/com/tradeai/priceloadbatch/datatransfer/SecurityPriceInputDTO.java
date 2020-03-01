package com.tradeai.priceloadbatch.datatransfer;




public class SecurityPriceInputDTO {
	
	private String date;
	private String close;
	
	public String getClose() {
		return close;
	}
	public void setClose(String close) {
		this.close = close;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	@Override
	public String toString() {
		return "SecurityPriceInputDTO [date=" + date + ", close=" + close + "]";
	}
	


	
	

}
