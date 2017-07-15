package de.zabuza.grawlox.data;

import java.util.ArrayList;

/**
 * Contains the results of a swearword examination.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class ExaminationResult {
	/**
	 * The input the results belong to.
	 */
	private final String mInput;
	/**
	 * List of the found swearword data.
	 */
	private final ArrayList<SwearWordData> mSwearWordData;

	/**
	 * Creates new initially empty examination results.
	 * 
	 * @param input
	 *            The input the results belong to.
	 */
	public ExaminationResult(final String input) {
		this.mInput = input;
		this.mSwearWordData = new ArrayList<>();
	}

	/**
	 * Adds a swearword to the results.
	 * 
	 * @param swearWord
	 *            The found swearword
	 * @param beginIndex
	 *            The index of the input where this swearword begins at
	 */
	public void addSwearWord(final String swearWord, final int beginIndex) {
		addSwearWord(new SwearWordData(swearWord, beginIndex));
	}

	/**
	 * Adds the given swearword data to the results.
	 * 
	 * @param swearWordData
	 *            The data to add
	 */
	public void addSwearWord(final SwearWordData swearWordData) {
		this.mSwearWordData.add(swearWordData);
	}

	/**
	 * Whether the examination result contains a swearword.
	 * 
	 * @return <tt>True</tt> if a swearword was found, <tt>false</tt> otherwise
	 */
	public boolean containsSwearWords() {
		return !this.mSwearWordData.isEmpty();
	}

	/**
	 * Gets the amount of detected swear words.
	 * 
	 * @return The amount of detected swear words
	 */
	public int getAmountOfDetectedSwearWords() {
		return this.mSwearWordData.size();
	}

	/**
	 * Gets the input this result belongs to.
	 * 
	 * @return The input this result belongs to
	 */
	public String getInput() {
		return this.mInput;
	}

	/**
	 * Gets the swearword data of this examination result.
	 * 
	 * @return The swearword data of this examination result
	 */
	public ArrayList<SwearWordData> getSwearWordData() {
		return this.mSwearWordData;
	}
}
