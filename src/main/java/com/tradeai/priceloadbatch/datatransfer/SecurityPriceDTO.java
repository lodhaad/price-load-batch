package com.tradeai.priceloadbatch.datatransfer;

import java.util.Date;

public class SecurityPriceDTO {
	
	private String stockId;
	private Double  price;
	private Date dateOfPrice;
	
	
	public SecurityPriceDTO() {
		
	}
	
	
	public SecurityPriceDTO(String stockId, Double price, Date dateOfPrice) {
		
		this.stockId = stockId;
		this.price = price;
		this.dateOfPrice = dateOfPrice;
		
	}
	
	
	public String getStockId() {
		return stockId;
	}
	public void setStockId(String stockId) {
		this.stockId = stockId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public Date getDateOfPrice() {
		return dateOfPrice;
	}
	public void setDateOfPrice(Date dateOfPrice) {
		this.dateOfPrice = dateOfPrice;
	}


	@Override
	public String toString() {
		return "SecurityPriceDTO [stockId=" + stockId + ", price=" + price + ", dateOfPrice=" + dateOfPrice + "]";
	}
	
	
	
	

}
