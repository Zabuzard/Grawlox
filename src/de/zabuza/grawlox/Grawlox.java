package de.zabuza.grawlox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import de.zabuza.grawlox.data.ExaminationResult;
import de.zabuza.grawlox.data.SwearWordData;
import de.zabuza.grawlox.util.MaxFinder;

/**
 * Grawlox is a profanity filter which offers methods for detecting and
 * replacing swearwords.<br>
 * Create an instance by using the factory methods, for example
 * {@link #createFromDefault()}. After creation {@link #filter(String)} can be
 * used to censor swearwords, {@link #detect(String)} to access all found
 * swearwords and {@link #isProfane(String)} to get whether the input contains
 * any swearwords.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class Grawlox {
	/**
	 * The symbol that indicates whether a line starting with it is a comment or
	 * not.
	 */
	public static final String COMMENT_SYMBOL = "#";
	/**
	 * The default path to the file containing all swearwords.
	 */
	public static final String DEFAULT_FILE_PATH = "swearwords.txt";
	/**
	 * The character to substitute swearwords with when censoring.
	 */
	public static final char DEFAULT_SUBSTITUTE = '*';
	/**
	 * Pattern that matches any non literal symbol.
	 */
	private static final String NON_LITERAL_PATTERN = "[^a-zA-Z]";

	/**
	 * Creates a new Grawlox instance from a swearword database at the default
	 * file path specified by {@link #DEFAULT_FILE_PATH}.
	 * 
	 * @return A new Grawlox instance with the given swearword database
	 * @throws IOException
	 *             If an I/O-Exception occurs while trying to process the
	 *             swearword database at the default file path
	 */
	public static Grawlox createFromDefault() throws IOException {
		return createFromSwearWordsPath(new File(DEFAULT_FILE_PATH).toPath());
	}

	/**
	 * Creates a new Grawlox instance from a swearword database at the given
	 * file.
	 * 
	 * @param swearWordsFile
	 *            The file containing the swearword database listing one
	 *            swearword per line
	 * @return A new Grawlox instance with the given swearword database
	 * @throws IOException
	 *             If an I/O-Exception occurs while trying to process the
	 *             swearword database at the given file
	 */
	public static Grawlox createFromSwearWordsPath(final File swearWordsFile) throws IOException {
		return createFromSwearWordsPath(swearWordsFile.toPath());
	}

	/**
	 * Creates a new Grawlox instance from a swearword database at the given
	 * path.
	 * 
	 * @param swearWordsPath
	 *            The path containing the swearword database listing one
	 *            swearword per line
	 * @return A new Grawlox instance with the given swearword database
	 * @throws IOException
	 *             If an I/O-Exception occurs while trying to process the
	 *             swearword database at the given path
	 */
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

	/**
	 * Creates a new Grawlox instance from a swearword database at the given
	 * path.
	 * 
	 * @param swearWordsPath
	 *            The path containing the swearword database listing one
	 *            swearword per line
	 * @return A new Grawlox instance with the given swearword database
	 * @throws IOException
	 *             If an I/O-Exception occurs while trying to process the
	 *             swearword database at the given path
	 */
	public static Grawlox createFromSwearWorldPath(final String swearWordsPath) throws IOException {
		return createFromSwearWordsPath(new File(swearWordsPath).toPath());
	}

	/**
	 * Prepares the given input for usage by lowering it, replacing some
	 * characters and removing leetspeak.
	 * 
	 * @param input
	 *            The input to prepare
	 * @return The prepared input
	 */
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

	/**
	 * The length of the largest swearword of this instance.
	 */
	private final int mLargestSwearWordLength;

	/**
	 * The set of swearwords of this instance.
	 */
	private final Set<String> mSwearWords;

	/**
	 * Creates a new instance with given swearwords.
	 * 
	 * @param swearWords
	 *            The set of swearwords for this instance
	 * @param largestSwearWordLength
	 *            The length of the largest swearword in the set
	 */
	public Grawlox(final Set<String> swearWords, final int largestSwearWordLength) {
		this.mSwearWords = swearWords;
		this.mLargestSwearWordLength = largestSwearWordLength;
	}

	/**
	 * Detects and returns all swearwords found in the given input.
	 * 
	 * @param input
	 *            The input to detect swearwords from
	 * @return A list of all detected swearwords
	 */
	public ArrayList<String> detect(final String input) {
		final ArrayList<SwearWordData> data = examine(input).getSwearWordData();

		final ArrayList<String> swearWords = new ArrayList<>(data.size());
		for (final SwearWordData dataEntry : data) {
			swearWords.add(dataEntry.getSwearWord());
		}

		return swearWords;
	}

	/**
	 * Examines the given input for swearwords and returns detailed results.<br>
	 * Same as calling {@link #examine(String, boolean) #examine(input, false)}.
	 * 
	 * @param input
	 *            The input to examine for swearwords
	 * @return Detailed results about detected swearwords
	 */
	public ExaminationResult examine(final String input) {
		return examine(input, false);
	}

	/**
	 * Examines the given input for swearwords and returns detailed results.
	 * 
	 * @param input
	 *            The input to examine for swearwords
	 * @param stopAtDetection
	 *            Whether the method should stop and return once the first
	 *            swearword has been detected
	 * @return Detailed results about detected swearwords
	 */
	public ExaminationResult examine(final String input, final boolean stopAtDetection) {
		final ExaminationResult results = new ExaminationResult(input);

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

	/**
	 * Filters the given input by censoring all detected swearwords with the
	 * default symbol specified by {@link #DEFAULT_SUBSTITUTE}.<br>
	 * Same as calling {@link #filter(String, char) #filter(input,
	 * DEFAULT_SUBSTITUTE)}.
	 * 
	 * @param input
	 *            The input to filter
	 * @return The filtered input
	 */
	public String filter(final String input) {
		return filter(input, DEFAULT_SUBSTITUTE);
	}

	/**
	 * Filters the given input by censoring all detected swearwords with the
	 * given substitute character.
	 * 
	 * @param input
	 *            The input to filter
	 * @param substitute
	 *            The character to substitute detected swearwords with
	 * @return The filtered input
	 */
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

	/**
	 * Whether the given input is profane, i.e. if it contains swearwords.
	 * 
	 * @param input
	 *            The input in question
	 * @return <tt>True</tt> if the input contains swearwords, <tt>false</tt>
	 *         otherwise
	 */
	public boolean isProfane(final String input) {
		return examine(input, true).containsSwearWords();
	}
}
