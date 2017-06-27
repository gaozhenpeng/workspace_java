package com.at.commonj.wm;

public class FibonacciWork implements commonj.work.Work {

    private long n = 0;
    private long fib = 0;

    public FibonacciWork(long n) {
        this.n = n;
    }

    public void release() {
    }

    public boolean isDaemon() {
        return false;
    }

    public void run() {
        fib = fib(n);
    }

    private int fib(long n) {
        if (n <= 0)
            return 0;
        if (n == 1)
            return 1;
        return fib(n - 1) + fib(n - 2);
    }

    public long getFib() {
        return fib;
    }

    public long getN() {
        return n;
    }

}