package com.at.commonj.wm;

import org.apache.log4j.Logger;

import commonj.work.WorkEvent;
import commonj.work.WorkListener;

public class FibonacciWorkListener implements WorkListener {
    private static final Logger logger = Logger.getLogger(FibonacciWorkListener.class);

    private int c = 0;

    public FibonacciWorkListener(int c) {
        this.c = c;
    }

    public void workAccepted(WorkEvent we) {
        logger.info("workAccepted: " + c);
    }

    public void workRejected(WorkEvent we) {
        logger.info("workRejected: " + c);
    }

    public void workStarted(WorkEvent we) {
        logger.info("workStarted: " + c);
    }

    public void workCompleted(WorkEvent we) {
        logger.info("workCompleted: " + c);
    }

}