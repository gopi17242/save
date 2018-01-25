package com.mail;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainClass {

    private static Timer timer = new Timer();
    static int fromMailNum =0;
    public static void main(String...args){
      //     timer.schedule (new MyTask(),0,1000*60*1);
    	
    	
    	Runnable drawRunnable = new Runnable() {
    	    public void run() {
    	        
    	     //   Test.test();
    	    //	ReadExcel_OneByOne_0.main_test(fromMailNum);
    	        System.out.println("fromMailNum="+fromMailNum);
    	        fromMailNum++;
    	    }
    	    
    	};
    	
    	ScheduledExecutorService exec = Executors.newScheduledThreadPool(1);
    	exec.scheduleAtFixedRate(drawRunnable , 0, 1, TimeUnit.MINUTES);
    	
    	
           
    }
}
