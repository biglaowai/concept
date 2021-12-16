package com.github.linyuzai.download.core.load;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@AllArgsConstructor
public class ExecutorSourceLoaderInvoker extends ConcurrentSourceLoaderInvoker {

    @Getter
    @Setter
    private Executor executor;

    @Override
    public void execute(Runnable runnable) {
        executor.execute(runnable);
    }

    public void shutdown() {
        if (executor instanceof ExecutorService) {
            ExecutorService executorService = (ExecutorService) executor;
            if (!executorService.isShutdown()) {
                executorService.shutdown();
            }
        }
    }
}