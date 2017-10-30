package com.hz.myspace.cms.util.test;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.hz.myspace.cms.util.scheduler.AbstractScheduler;

public class TestSchedueler {

	public static void main(String[] args) {
		ScheduledExecutorService schedulerPool=Executors.newScheduledThreadPool(10);
		schedulerPool.scheduleAtFixedRate(new TestJob(), 0, 2000, TimeUnit.MILLISECONDS);
	}
	
	public static class TestJob extends AbstractScheduler{

		@Override
		public void doScheduleJob() {
			System.out.println("doScheduleJob");
		}
		
	} 
}
