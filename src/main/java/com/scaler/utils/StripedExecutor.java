package com.scaler.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class StripedExecutor {
    private List<ExecutorService> executor;

    public StripedExecutor(int numberOfThreads) {
        executor = new ArrayList<>();

        for (int i = 0; i < numberOfThreads; ++i) {
            executor.add(
                    Executors.newSingleThreadExecutor()
            );
        }
    }

    public Future<Void> submit(int id, Runnable task) {
        return CompletableFuture.runAsync(
                task,
                executor.get(id)
        );
    }
}
