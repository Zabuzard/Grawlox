package de.zabuza.grawlox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public final class Grawlox {
	public static final String COMMENT_SYMBOL = "#";
	public static final String DEFAULT_FILE_PATH = "swearWords.txt";
	public static final char DEFAULT_SUBSTITUTE = '*';
	private static final String NON_LITERAL_PATTERN = "[^a-zA-Z]";

	public static Grawlox createFromDefault() throws IOException {
		return createFromSwearWordsPath(new File(DEFAULT_FILE_PATH).toPath());
	}

	public static Grawlox createFromSwearWordsPath(final File swearWordsFile) throws IOException {
		return createFromSwearWordsPath(swearWordsFile.toPath());
	}

	public static Grawlox createFromSwearWordsPath(final Path swearWordsPath) throws IOException {
		final HashSet<String> swearWords = new HashSet<>();
		final MaxFinder maxFinder = new MaxFinder(0);

		Files.lines(swearWordsPath).forEach(line -> {
			// Only add word if it is no comment
			if (!line.startsWith(COMMENT_SYMBOL)) {
				swearWords.add(prepareInput(line));

				// Find the largest swearword
				maxFinder.announceValue(line.length());
			}
		});

		return new Grawlox(swearWords, maxFinder.getMaxValue());
	}

	public static Grawlox createFromSwearWorldPath(final String swearWordsPath) throws IOException {
		return createFromSwearWordsPath(new File(swearWordsPath).toPath());
	}

	/**
	 * Demonstrates the usage of Grawlox.
	 * 
	 * @param args
	 *            Not supported
	 * @throws IOException
	 *             If an I/O-Exception occurs
	 */
	public static void main(final String[] args) throws IOException {
		final String input = "Hello you f0o‰÷bar you are fo---ooo b4r...";
		final Grawlox grawlox = createFromDefault();

		System.out.println("Input is:\t" + input);
		System.out.println("Is profane:\t" + grawlox.isProfane(input));
		System.out.println("Detected:\t" + grawlox.detect(input));
		System.out.println("Filtered:\t" + grawlox.filter(input));
	}

	public static String prepareInput(final String input) {
		final char[] inputChars = input.toLowerCase().toCharArray();

		// Remove leetspeak
		for (int i = 0; i < inputChars.length; i++) {
			final char currentChar = inputChars[i];
			if (currentChar == '4' || currentChar == '@') {
				inputChars[i] = 'a';
			} else if (currentChar == '8') {
				inputChars[i] = 'b';
			} else if (currentChar == '3') {
				inputChars[i] = 'e';
			} else if (currentChar == '6' || currentChar == '9') {
				inputChars[i] = 'g';
			} else if (currentChar == '1' || currentChar == '!') {
				inputChars[i] = 'i';
			} else if (currentChar == '0') {
				inputChars[i] = 'o';
			} else if (currentChar == '5') {
				inputChars[i] = 's';
			} else if (currentChar == '7') {
				inputChars[i] = 't';
			} else if (currentChar == '2') {
				inputChars[i] = 'z';
			}
		}

		// Remove all non literal characters
		final String result = new String(inputChars).replaceAll(NON_LITERAL_PATTERN, "");
		return result;
	}

	private final int mLargestSwearWordLength;

	private final Set<String> mSwearWords;

	public Grawlox(final Set<String> swearWords, final int largestSwearWordLength) {
		this.mSwearWords = swearWords;
		this.mLargestSwearWordLength = largestSwearWordLength;
	}

	public ArrayList<String> detect(final String input) {
		final ArrayList<SwearWordData> data = examine(input).getSwearWordData();

		final ArrayList<String> swearWords = new ArrayList<>(data.size());
		for (final SwearWordData dataEntry : data) {
			swearWords.add(dataEntry.getSwearWord());
		}

		return swearWords;
	}

	public ExaminationResults examine(final String input) {
		return examine(input, false);
	}

	public ExaminationResults examine(final String input, final boolean stopAtDetection) {
		final ExaminationResults results = new ExaminationResults(input);

		// Iterate over each letter of the input
		for (int i = 0; i < input.length(); i++) {
			// From each letter try to find a swearword until reaching the end
			// or exceeding the maximal size of a swearword
			for (int offset = 0; offset < input.length() - i; offset++) {
				// Unfortunately substring introduces a time complexity of
				// O(n^2) in total because it creates a copy in O(n) instead of
				// sharing the char-Array now. However creating a sharable
				// String class is no option since we then loose the heavy
				// String literal optimizations that would make it even slower
				// than the current solution.
				final String potentialSwearWord = input.substring(i, i + offset + 1);
				final String potentialSwearWordPrepared = prepareInput(potentialSwearWord);
				if (this.mSwearWords.contains(potentialSwearWordPrepared)) {
					// The word is indeed a swearword
					results.addSwearWord(potentialSwearWord, i);

					if (stopAtDetection) {
						return results;
					}
					break;
				}

				// If prepared word exceeds the largest swearword it can not
				// become one when further increasing the offset
				if (potentialSwearWordPrepared.length() >= this.mLargestSwearWordLength) {
					break;
				}
			}
		}

		return results;
	}

	public String filter(final String input) {
		return filter(input, DEFAULT_SUBSTITUTE);
	}

	public String filter(final String input, final char substitute) {
		final ArrayList<SwearWordData> data = examine(input).getSwearWordData();

		final char[] inputChars = input.toCharArray();
		for (final SwearWordData dataEntry : data) {
			// Replace the swearword by the substitute
			final int begin = dataEntry.getBeginIndex();
			final int length = dataEntry.getSwearWord().length();

			for (int i = begin; i < begin + length; i++) {
				inputChars[i] = substitute;
			}
		}

		return new String(inputChars);
	}

	public boolean isProfane(final String input) {
		return examine(input, true).containsSwearWords();
	}
}
