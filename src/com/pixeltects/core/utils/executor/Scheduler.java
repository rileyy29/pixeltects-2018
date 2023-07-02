package com.pixeltects.core.utils.executor;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
public class Scheduler {

  private static ListeningExecutorService publicService;
  
  static {
    ThreadPoolExecutor service = new ThreadPoolExecutor(5, 10, 0L, TimeUnit.MILLISECONDS, Queues.newLinkedBlockingQueue(), new ThreadFactory() {
          private final AtomicInteger counter = new AtomicInteger();
          
          public Thread newThread(Runnable r) {
            String name = "Thread " + this.counter.incrementAndGet();
            return new Thread(r, name);
          }
        });
    service.setKeepAliveTime(20L, TimeUnit.MINUTES);
    service.allowCoreThreadTimeOut(true);
    service.prestartAllCoreThreads();
    publicService = MoreExecutors.listeningDecorator(service);
  }
  
  public static ListeningExecutorService getPublicExecutor() {
    return publicService;
  }
  
  public static <T> void run(Callable<T> task, FutureCallback<? super T> callback) {
    try {
      T result = task.call();
      try {
        callback.onSuccess(result);
      } catch (Throwable cause) {
        cause.printStackTrace();
      } 
    } catch (Throwable cause) {
      callback.onFailure(cause);
    } 
  }
  
  public static <T> void runAsync(Callable<T> task, FutureCallback<? super T> callback) {
    runAsync(() -> run(task, callback));
  }
  
  public static void runAsync(Runnable task) {
    publicService.submit(task);
  }
}