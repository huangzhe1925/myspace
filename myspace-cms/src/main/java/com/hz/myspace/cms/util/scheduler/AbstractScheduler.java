package com.hz.myspace.cms.util.scheduler;

public abstract class AbstractScheduler implements Scheduler{

	public static final Long DEFAULT_SLEEP_DURATION=1000L;
	
	public static final Long DEFAULT_PERIOD_DURATION=60000L;
	
	@Override
	public void run() {
		try {
			Thread.sleep(DEFAULT_SLEEP_DURATION);
			doScheduleJob();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
