package com.nbicc.utils;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2017/6/20 0020.
 */

public class TimerUtil {
    /**
     * ScheduledExecutorService是从Java SE5的java.util.concurrent里，做为并发工具类被引进的，这是最理想的定时任务实现方式。
     * 相比于上两个方法，它有以下好处：
     * 1>相比于Timer的单线程，它是通过线程池的方式来执行任务的
     * 2>可以很灵活的去设定第一次执行任务delay时间
     * 3>提供了良好的约定，以便设定执行的时间间隔
     *
     * 下面是实现代码，我们通过ScheduledExecutorService#scheduleAtFixedRate展示这个例子，通过代码里参数的控制，首次执行加了delay时间。
     */

    public static void Timer() {
            Runnable runnable = new Runnable() {
                public void run() {
                    // task to run goes here
                    System.out.println("Hello !!");
//                    BatchSyn bs = new BatchSyn();
//                    bs.batSyn("zmkm");
//                    com.bugtrapper01.transfer.rest.BatchSyn bs = new com.bugtrapper01.transfer.rest.BatchSyn();

                }
            };
            ScheduledExecutorService service = Executors
                    .newSingleThreadScheduledExecutor();
            // 第二个参数为首次执行的延时时间，第三个参数为定时执行的间隔时间
            service.scheduleAtFixedRate(runnable, 10, 1, TimeUnit.SECONDS);
     }

}
