package com.tradeai.priceloadbatch.steps;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class GetPriceTask implements Tasklet, PriceLoadTask {
	
	private static Logger logger = LoggerFactory.getLogger(GetPriceTask.class);

	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
		logger.info("MyTaskOne start..");
		
		executeIndependentTask();

		logger.info("MyTaskOne done..");


		return RepeatStatus.FINISHED;
	}

	private Boolean createFile(String fileName, String content) throws IOException {
    	
    	
		
		Boolean returnValue = Boolean.FALSE;
    	BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
    	writer.write(content);
    	writer.close();
    	
    	returnValue=Boolean.TRUE;
    	
    	return returnValue;
    }

	@Override
	public Boolean executeIndependentTask() throws ClientProtocolException, IOException {
		
		HashMap<String,String> mapping = getSecCodeMapping();
		
		Boolean returnVal = new Boolean(false);
		
		String startDate = getMaxDateInString();
		String toDate = getCurrentDayInString();
		
		Set<String> keys = mapping.keySet();
		
		CloseableHttpClient httpClient = HttpClients.createDefault();
		
		for (String key : keys) {
			
			System.out.println(key);
			
			String bseCode = mapping.get(key);
			
			try {

				HttpGet request = new HttpGet(
						"https://www.quandl.com/api/v3/datasets/BSE/BOM"+bseCode+".csv?start_date="+startDate+"&end_date="+toDate+"&column_index=4&api_key=k61YFfuWGDoWvDxscUK3");

				CloseableHttpResponse response = httpClient.execute(request);

				try {

					// Get HttpResponse Status
					logger.info("this is the response for protocol {}", response.getProtocolVersion()); // HTTP/1.1
					logger.info("this is the response for statuscode {}", response.getStatusLine().getStatusCode()); // 200
					logger.info("this is the response for reason phase {}", response.getStatusLine().getReasonPhrase()); // OK
					logger.info("this is the response forstatus line {}", response.getStatusLine().toString()); // HTTP/1.1 200 OK

					HttpEntity entity = response.getEntity();
					if (entity != null) {
						// return it as a String
						String result = EntityUtils.toString(entity);
						logger.info("this is the response forresults", result);
						
						String fileName = "src/main/resources/"+key+".csv";
						Boolean fileCreated = createFile(fileName, result);
						
						if (fileCreated) {
							System.out.println("file created");
						}
					}

				} finally {
					response.close();
				}
			} finally {
				
			}
			
			
		} ///for loop
		

		httpClient.close();
		
		returnVal = true;

		
		return returnVal;
	}
	
	
	
	private List<LocalDate> getDateGap(String date1, String date2 ) {


		LocalDate start = LocalDate.parse(date1);
		LocalDate end = LocalDate.parse(date2);
		List<LocalDate> totalDates = new ArrayList<>();
		while (!start.isAfter(end)) {
		    totalDates.add(start);
		    start = start.plusDays(1);
		}
		
		return totalDates;
	}
	
	
	private String getMaxDateInString() {
		
		return "2019-01-01";
		
	}
	
	private String getCurrentDayInString() {
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.now();
		String date = dtf.format(localDate); //2016/11/16
		
		return date;
	}
	
	
	private HashMap<String,String> getSecCodeMapping(){
		
		HashMap<String,String> secCodeMapping =  new HashMap<String,String>();
		
		secCodeMapping.put("TCS", "532540");
		secCodeMapping.put("INFY", "500209");
		
		return secCodeMapping;
		
		
		
	}
}
