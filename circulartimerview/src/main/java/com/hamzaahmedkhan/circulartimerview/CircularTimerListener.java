package com.hamzaahmedkhan.circulartimerview;

/**
 * The interface Circular timer listener.
 */
public interface CircularTimerListener {
    /**
     * Update data on tick string.
     *
     * @param remainingTimeInMs the remaining time in ms
     * @return the string
     */
    String updateDataOnTick(long remainingTimeInMs);

    /**
     * On timer finished.
     */
    void onTimerFinished();
}
