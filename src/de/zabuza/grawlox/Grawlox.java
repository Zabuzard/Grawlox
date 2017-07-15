package de.zabuza.grawlox;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

public final class Grawlox {
	public static final String COMMENT_SYMBOL = "#";
	public static final String DEFAULT_FILE_PATH = "swearWords.txt";

	private static Set<String> readSwearWordsFromPath(final Path swearWordsPath) throws IOException {
		final HashSet<String> swearWords = new HashSet<>();

		Files.lines(swearWordsPath).forEach(line -> {
			// Only add word if it is no comment
			if (!line.startsWith(COMMENT_SYMBOL)) {
				swearWords.add(line);
			}
		});

		return swearWords;
	}

	private final Set<String> mSwearWords;

	public Grawlox() throws IOException {
		this(DEFAULT_FILE_PATH);
	}

	public Grawlox(final File swearWordsFile) throws IOException {
		this(readSwearWordsFromPath(swearWordsFile.toPath()));
	}

	public Grawlox(final Path swearWordsPath) throws IOException {
		this(readSwearWordsFromPath(swearWordsPath));
	}

	public Grawlox(final Set<String> swearWords) {
		this.mSwearWords = swearWords;
	}

	public Grawlox(final String filePath) throws IOException {
		this(new File(filePath).toPath());
	}

	public ExaminationResults examine(String input) {
		final ExaminationResults results = new ExaminationResults(input);

		return results;
	}
}
