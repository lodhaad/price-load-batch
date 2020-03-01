package com.tradeai.priceloadbatch.datamodel;



import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;


@Entity
@Table (name ="price" , schema = "price")

public class SecurityPrice {
	
	@Id
	@Column(name="security_code")
	private String stockId;
	
	@Column(name="eod_price")
	private Double price;
	
	@Column(name = "date_of_price")
	private Date dateOfPrice;
	
	
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
	

}

