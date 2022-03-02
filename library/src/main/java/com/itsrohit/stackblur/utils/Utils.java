package com.itsrohit.stackblur.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Utils {
	
    public static final int EXECUTOR_THREADS = Runtime.getRuntime().availableProcessors();
	public static final ExecutorService EXECUTOR = Executors.newFixedThreadPool(EXECUTOR_THREADS);
	
    private Utils() {
	}
}
