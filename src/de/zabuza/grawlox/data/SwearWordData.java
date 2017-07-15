package de.zabuza.grawlox.data;

/**
 * Contains data about a found swearword.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class SwearWordData implements Comparable<SwearWordData> {
	/**
	 * The index within the input where this swearword begins at.
	 */
	private final int mBeginIndex;
	/**
	 * The swearword of this data.
	 */
	private final String mSwearWord;

	/**
	 * Creates a new swearword data object.
	 * 
	 * @param swearWord
	 *            The swearword of this data
	 * @param beginIndex
	 *            The index within the input where this swearword begins at
	 */
	public SwearWordData(final String swearWord, final int beginIndex) {
		this.mSwearWord = swearWord;
		this.mBeginIndex = beginIndex;
	}

	/**
	 * Orders ascending by comparing the </tt>beginIndex<tt> given with
	 * {@link #getBeginIndex()}.
	 */
	@Override
	public int compareTo(final SwearWordData other) {
		return Integer.compare(getBeginIndex(), other.getBeginIndex());
	}

	/**
	 * Gets the index within the input where this swearword begins at.
	 * 
	 * @return The index within the input where this swearword begins at
	 */
	public int getBeginIndex() {
		return this.mBeginIndex;
	}

	/**
	 * Gets the swearword of this data.
	 * 
	 * @return The swearword of this data
	 */
	public String getSwearWord() {
		return this.mSwearWord;
	}
}
