package com.at.spring.spring_quartz.quartz.job;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.quartz.QuartzJobBean;

public class SpringHelloworldJob extends QuartzJobBean{
	private static final Logger logger = LoggerFactory.getLogger(SpringHelloworldJob.class);

	@Override
	protected void executeInternal(JobExecutionContext arg0) throws JobExecutionException {
		logger.info("Helloworld!");
	}

}
