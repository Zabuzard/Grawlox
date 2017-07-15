package de.zabuza.grawlox;

public final class MaxFinder {

	private int mMaxValue;

	public MaxFinder() {
		this(Integer.MIN_VALUE);
	}

	public MaxFinder(final int initialMax) {
		this.mMaxValue = initialMax;
	}

	public boolean announceValue(final int value) {

		final boolean isGreater = value > this.mMaxValue;
		if (isGreater) {
			this.mMaxValue = value;
		}

		return isGreater;
	}

	public int getMaxValue() {
		return this.mMaxValue;
	}
}
