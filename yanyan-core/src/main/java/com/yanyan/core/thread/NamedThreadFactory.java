package com.yanyan.core.thread;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * copy from {@link net.sf.ehcache.util.NamedThreadFactory}
 * User: Saintcy
 * Date: 2016/12/29
 * Time: 15:17
 */
public class NamedThreadFactory implements ThreadFactory {

    private static AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    /**
     * Constructor accepting the prefix of the threads that will be created by this {@link ThreadFactory}
     *
     * @param namePrefix
     *            Prefix for names of threads
     */
    public NamedThreadFactory(String namePrefix) {
        this.namePrefix = namePrefix;
    }

    /**
     * Returns a new thread using a name as specified by this factory {@inheritDoc}
     */
    public Thread newThread(Runnable runnable) {
        return new Thread(runnable, namePrefix + " thread-" + threadNumber.getAndIncrement());
    }
}
