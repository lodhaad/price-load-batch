package com.tradeai.priceloadbatch.repository;


import java.sql.Date;


import org.springframework.data.repository.CrudRepository;

import com.tradeai.priceloadbatch.datamodel.SecurityPrice;




public interface SecurityPriceRepository extends CrudRepository<SecurityPrice, String> {
	
	public SecurityPrice findByStockIdAndDateOfPrice(String stockId, Date dateOfPrice);

}

