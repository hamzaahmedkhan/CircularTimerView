package com.hamzaahmedkhan.circulartimerview;

/**
 * The enum Time format enum.
 */
public enum TimeFormatEnum {
    /**
     * Millis time format enum.
     */
    MILLIS,
    /**
     * Seconds time format enum.
     */
    SECONDS,
    /**
     * Minutes time format enum.
     */
    MINUTES,
    /**
     * Hour time format enum.
     */
    HOUR,
    /**
     * Day time format enum.
     */
    DAY;

    /**
     * Canonical form string.
     *
     * @return the string
     */
    public String canonicalForm() {
        return this.name();
    }

    /**
     * From canonical form time format enum.
     *
     * @param canonical the canonical
     * @return the time format enum
     */
    public static TimeFormatEnum fromCanonicalForm(String canonical) {
        return valueOf(TimeFormatEnum.class, canonical);
    }
}