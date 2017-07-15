package de.zabuza.grawlox.util;

/**
 * Utility class that finds the maximum of given values.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class MaxFinder {
	/**
	 * The current maximum of announced values.
	 */
	private int mMaxValue;

	/**
	 * Creates a new max finder with the initial maximum set to
	 * {@link Integer#MIN_VALUE}.
	 */
	public MaxFinder() {
		this(Integer.MIN_VALUE);
	}

	/**
	 * Creates a new max finder with the given value as initial maximum.
	 * 
	 * @param initialMax
	 *            The value to use as initial maximum
	 */
	public MaxFinder(final int initialMax) {
		this.mMaxValue = initialMax;
	}

	/**
	 * Announces the given value to this finder. The finder will memorize the
	 * maximum of all announced values.
	 * 
	 * @param value
	 *            The value to announce
	 * @return <tt>True</tt> if the given value was greater than the previous
	 *         known maximum, <tt>false</tt> otherwise
	 */
	public boolean announceValue(final int value) {

		final boolean isGreater = value > this.mMaxValue;
		if (isGreater) {
			this.mMaxValue = value;
		}

		return isGreater;
	}

	/**
	 * Gets the current maximum of all announced values.
	 * 
	 * @return The current maximum of all announced values
	 */
	public int getMaxValue() {
		return this.mMaxValue;
	}
}
