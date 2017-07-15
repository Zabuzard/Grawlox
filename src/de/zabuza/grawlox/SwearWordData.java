package de.zabuza.grawlox;

public class SwearWordData implements Comparable<SwearWordData> {
	private final int mBeginIndex;
	private final String mSwearWord;

	public SwearWordData(final String swearWord, final int beginIndex) {
		this.mSwearWord = swearWord;
		this.mBeginIndex = beginIndex;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(final SwearWordData other) {
		return Integer.compare(this.mBeginIndex, other.mBeginIndex);
	}

	public int getBeginIndex() {
		return this.mBeginIndex;
	}

	public String getSwearWord() {
		return this.mSwearWord;
	}
}
