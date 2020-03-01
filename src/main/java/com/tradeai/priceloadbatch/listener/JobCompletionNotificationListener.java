package com.tradeai.priceloadbatch.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.tradeai.priceloadbatch.datatransfer.SecurityPriceDTO;


@Component
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {
	
	 private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

	  private final JdbcTemplate jdbcTemplate;

	  @Autowired
	  public JobCompletionNotificationListener(JdbcTemplate jdbcTemplate) {
	    this.jdbcTemplate = jdbcTemplate;
	  }

	  @Override
	  public void afterJob(JobExecution jobExecution) {
	    if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
	      log.info("!!! JOB FINISHED! Time to verify the results");

	      jdbcTemplate.query(" SELECT  s.* FROM    price.price s INNER JOIN\r\n" + 
	      		"        (\r\n" + 
	      		"            SELECT  security_code,\r\n" + 
	      		"                    MAX(date_of_price) MTotal\r\n" + 
	      		"            FROM    price.price\r\n" + 
	      		"            GROUP BY security_code\r\n" + 
	      		"        ) sMax  ON  s.security_code = sMax.security_code \r\n" + 
	      		"                AND s.date_of_price = sMax.MTotal ",
	        (rs, row) -> 
	         new SecurityPriceDTO(
	        			
	        			rs.getString(1),
	        			rs.getDouble(2),
	        			rs.getDate(3) )
	        	
	        	
	        


	      ).forEach(person -> log.info("Found <" + person + "> in the database."));
	    }
	  }

	

}
