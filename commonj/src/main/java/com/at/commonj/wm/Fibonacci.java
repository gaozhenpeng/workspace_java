package com.at.commonj.wm;

import java.util.ArrayList;
import java.util.Collection;

import org.apache.log4j.Logger;

import commonj.work.WorkException;
import commonj.work.WorkItem;
import commonj.work.WorkManager;

import de.myfoo.commonj.util.ThreadPool;

public class Fibonacci {
    private static final Logger logger = Logger.getLogger(Fibonacci.class);

    private static ThreadPool getThreadPool(int min, int max, int queueLength){
        return new ThreadPool(min, max, queueLength);
    }

    public static void main(String[] args) throws IllegalArgumentException, WorkException, InterruptedException{
        WorkManager wm = null;
        //Context ctx = new InitialContext();
        //wm = (WorkManager) ctx.lookup("java:/comp/env/wm/FooWorkManager");
        ThreadPool pool = getThreadPool(3, 10, 1000);
        wm = new de.myfoo.commonj.work.FooWorkManager(pool);

        Collection<WorkItem> items = new ArrayList<WorkItem>();
        items.add(wm.schedule(new FibonacciWork(11L), new FibonacciWorkListener(1)));
        items.add(wm.schedule(new FibonacciWork(22L), new FibonacciWorkListener(2)));
        items.add(wm.schedule(new FibonacciWork(33L), new FibonacciWorkListener(3)));
        items.add(wm.schedule(new FibonacciWork(44L), new FibonacciWorkListener(4)));
        items.add(wm.schedule(new FibonacciWork(33L), new FibonacciWorkListener(5)));
        items.add(wm.schedule(new FibonacciWork(22L), new FibonacciWorkListener(6)));
        items.add(wm.schedule(new FibonacciWork(11L), new FibonacciWorkListener(7)));

        wm.waitForAll(items, 30000);
        for(WorkItem wi : items){
            FibonacciWork w = (FibonacciWork)wi.getResult();
            logger.info("Fib(" + w.getN() + "): " + w.getFib());
        }

        pool.shutdown();
        Object o = new Object();
        synchronized (o) {
            o.wait(1000);
        }
        pool.forceShutdown();
        synchronized (o) {
            o.wait(1000);
        }
    }
}