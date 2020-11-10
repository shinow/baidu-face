package org.springblade.common.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class JdkThreadPoolConfig {

    //不限线程数上限的线程池
    public static  final  ExecutorService cachedThreadPool = Executors.newWorkStealingPool();

}
