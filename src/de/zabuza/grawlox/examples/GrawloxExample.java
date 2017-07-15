package de.zabuza.grawlox.examples;

import java.io.IOException;

import de.zabuza.grawlox.Grawlox;

/**
 * Utility class that demonstrates the usage of Grawlox in its
 * {@link #main(String[])} method.
 * 
 * @author Zabuza {@literal <zabuza.dev@gmail.com>}
 *
 */
public final class GrawloxExample {
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
		final Grawlox grawlox = Grawlox.createFromDefault();

		System.out.println("Input is:\t" + input);
		System.out.println("Is profane:\t" + grawlox.isProfane(input));
		System.out.println("Detected:\t" + grawlox.detect(input));
		System.out.println("Filtered:\t" + grawlox.filter(input));
	}

	/**
	 * Utility class. No implementation.
	 */
	private GrawloxExample() {

	}
}
