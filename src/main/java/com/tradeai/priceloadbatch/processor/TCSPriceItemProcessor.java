package com.tradeai.priceloadbatch.processor;




import java.text.SimpleDateFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.tradeai.priceloadbatch.datatransfer.SecurityPriceInputDTO;
import com.tradeai.priceloadbatch.datatransfer.SecurityPriceDTO;

public class TCSPriceItemProcessor implements ItemProcessor<SecurityPriceInputDTO, SecurityPriceDTO> {

	  private static final Logger log = LoggerFactory.getLogger(TCSPriceItemProcessor.class);

	  @Override
	  public SecurityPriceDTO process(final SecurityPriceInputDTO asset) throws Exception {
		  

		
	    
	    final SecurityPriceDTO newAsset = new SecurityPriceDTO();
	    
	    newAsset.setStockId("TCS");
	    
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    
	    newAsset.setDateOfPrice(df.parse(asset.getDate()));
	    
	    
	    newAsset.setPrice(Double.parseDouble(asset.getClose()));

	    log.info("Converting (" + asset + ") into (" + newAsset + ")");

	    return newAsset;
	  }
}
