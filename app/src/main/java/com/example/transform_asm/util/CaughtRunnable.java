package com.example.transform_asm.util;

public class CaughtRunnable implements Runnable {

    private final Runnable mRunnable;

    public CaughtRunnable(final Runnable runnable) {
        this.mRunnable = runnable;
    }

    @Override
    public void run() {
        try {
            this.mRunnable.run();
        } catch (final RuntimeException e) {
            // ignore
        }
    }
}
