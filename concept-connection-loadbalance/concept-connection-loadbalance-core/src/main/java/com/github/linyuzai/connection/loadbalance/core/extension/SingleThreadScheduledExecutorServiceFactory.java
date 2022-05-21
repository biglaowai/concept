package com.github.linyuzai.connection.loadbalance.core.extension;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * 使用 {@link Executors#newSingleThreadScheduledExecutor()} 创建 {@link ScheduledExecutorService}
 */
public class SingleThreadScheduledExecutorServiceFactory implements ScheduledExecutorServiceFactory {

    @Override
    public ScheduledExecutorService create(Object key) {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
