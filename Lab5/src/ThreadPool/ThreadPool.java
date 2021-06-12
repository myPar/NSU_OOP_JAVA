package ThreadPool;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ThreadPool {
    private ThreadPoolExecutor threadPool;
    // minThreadCount users can do nothing, but other users are typing messages, so new threads should handle them
    private static final int maxQueueSize = 1;
    private static final int minThreadCount = 10;
    private static final int maxThreadCount = Integer.MAX_VALUE;
// constructor:
    public ThreadPool(int maxClientsCount) {
        // create fixed size blocking queue
        BlockingQueue<Runnable> taskQueue = new LinkedBlockingQueue<>(maxQueueSize);
        // create thread pool
        threadPool = new ThreadPoolExecutor(minThreadCount, maxThreadCount, 10, TimeUnit.MILLISECONDS, taskQueue);
    }
// add task new Task method
    public synchronized void addTask(Task task) {
        threadPool.execute(task);
    }
}
