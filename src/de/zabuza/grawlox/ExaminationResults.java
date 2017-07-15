package de.zabuza.grawlox;

import java.util.ArrayList;

public final class ExaminationResults {
	private final String mInput;
	private final ArrayList<SwearWordData> mSwearWordData;

	public ExaminationResults(final String input) {
		this.mInput = input;
		this.mSwearWordData = new ArrayList<>();
	}

	public void addSwearWord(final String swearWord, final int beginIndex) {
		addSwearWord(new SwearWordData(swearWord, beginIndex));
	}

	public void addSwearWord(final SwearWordData swearWordData) {
		this.mSwearWordData.add(swearWordData);
	}

	public int amountOfDetectedSwearWords() {
		return this.mSwearWordData.size();
	}

	public boolean containsSwearWords() {
		return this.mSwearWordData.isEmpty();
	}
}
