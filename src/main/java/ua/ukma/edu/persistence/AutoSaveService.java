package ua.ukma.edu.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.ukma.edu.domain.University;

import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class AutoSaveService {
    private static final Logger logger = LoggerFactory.getLogger(AutoSaveService.class);

    private final UniversityStorage storage;
    private final Supplier<University> universitySupplier;
    private final ScheduledExecutorService executor;
    private final AtomicBoolean dirty = new AtomicBoolean(false);
    private final long intervalSeconds;

    public AutoSaveService(UniversityStorage storage, Supplier<University> universitySupplier, long intervalSeconds) {
        this.storage = Objects.requireNonNull(storage, "Storage cannot be null.");
        this.universitySupplier = Objects.requireNonNull(universitySupplier, "University supplier cannot be null.");
        if (intervalSeconds <= 0)
            throw new IllegalArgumentException("Autosave interval must be > 0 seconds.");
        this.intervalSeconds = intervalSeconds;

        ThreadFactory threadFactory = runnable -> {
            Thread thread = new Thread(runnable, "autosave-thread");
            thread.setDaemon(true);
            return thread;
        };
        this.executor = Executors.newSingleThreadScheduledExecutor(threadFactory);
    }

    public void start() {
        executor.scheduleWithFixedDelay(this::saveIfDirty, intervalSeconds, intervalSeconds, TimeUnit.SECONDS);
    }

    public void markDirty() {
        dirty.set(true);
    }

    public void flushNow() {
        if (dirty.compareAndSet(true, false)) {
            storage.save(universitySupplier.get());
            logger.info("Autosave flushed current changes.");
        }
    }

    public void stop() {
        flushNow();
        executor.shutdown();
        try {
            if (!executor.awaitTermination(3, TimeUnit.SECONDS))
                executor.shutdownNow();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            executor.shutdownNow();
        }
    }

    private void saveIfDirty() {
        try {
            if (dirty.compareAndSet(true, false)) {
                storage.save(universitySupplier.get());
                logger.info("Autosave completed.");
            }
        } catch (Exception e) {
            logger.error("Autosave failed.", e);
            dirty.set(true);
        }
    }
}

