package com.tradeai.priceloadbatch.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.tradeai.priceloadbatch.steps.GetPriceTask;


@Configuration
@EnableBatchProcessing
public class GetPriceConfig {
	
	 @Autowired
	    private JobBuilderFactory jobs;
	 
	    @Autowired
	    private StepBuilderFactory steps;
	     
	    @Bean
	    public Step stepOne(){
	        return steps.get("stepOne")
	                .tasklet(new GetPriceTask())
	                .build();
	    }
	    
	    
	    @Bean
	    public Job demoJob(){
	        return jobs.get("get Price Job")
	                .incrementer(new RunIdIncrementer())
	                .start(stepOne())
	                ///.next(stepTwo())
	                .build();
	    }
	    
	    
	    ///get the max date in price 
	    ///get current date 
	    ///find the missing dates - needs max and current dates and stores missing dates 
	   ///find the mapping of stock with quandl
	    ///create a URL for those days for one stock 
	     ///load the dates into the file for that stock 
	    ///merge the files into prices.csv file 
	    
	    ///input files 
	    ///processor
	    ///output into the data base  

}
