package ru.eternity074;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Main {
	public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
	private static StringBuilder sb = new StringBuilder();

	public static void main(String[] args) {

		Map<String, String> mapArgs = readArgs(args);

		String mode = mapArgs.getOrDefault("-mode", "enc");
		int key = Integer.parseInt(mapArgs.getOrDefault("-key", "0"));
		String data = mapArgs.getOrDefault("-data", "");
		String in = data.equals("") ? mapArgs.getOrDefault("-in", data) : data;
		String out = mapArgs.getOrDefault("-out", "sout");
		String alg = mapArgs.getOrDefault("-alg", "shift");

		try {
			String str;
			String encoded;
			String decoded;

			switch (mode) {
			case "enc": {
				if (!in.equals(data)) {
					str = readFileAsString(in);
					if (alg.equals("shift")) {
						encoded = encodeShift(str, key);
					} else {
						encoded = encodeUnicode(str, key);
					}
				} else {
					if (alg.equals("shift")) {
						encoded = encodeShift(data, key);
					} else {
						encoded = encodeUnicode(data, key);
					}
				}

				if (out.equals("sout")) {
					System.out.println(encoded);
				} else {
					writeToFile(out, encoded);
				}

				break;
			}
			case "dec": {
				if (!in.equals(data)) {
					str = readFileAsString(in);
					if (alg.equals("shift")) {
						decoded = decodeShift(str, key);
					} else {
						decoded = decodeUnicode(str, key);
					}
				} else {
					if (alg.equals("shift")) {
						decoded = decodeShift(data, key);
					} else {
						decoded = decodeUnicode(data, key);
					}
				}

				if (out.equals("sout")) {
					System.out.println(decoded);
				} else {
					writeToFile(out, decoded);
				}

				break;
			}
			default: {
				System.out.println("Unknown mode");
			}
			}
		} catch (Exception e) {
			System.out.println("Error! Something went wrong!");
		}

	}

	private static Map<String, String> readArgs(String[] args) {
		Map<String, String> map = new HashMap<>();
		for (int i = 0, j = 1; j < args.length; i += 2, j += 2) {
			map.put(args[i], args[j]);
		}
		return map;
	}

	private static void writeToFile(String filename, String data) throws IOException {
		Path path = Paths.get(filename);
		byte[] strToBytes = data.getBytes();
		Files.write(path, strToBytes);
	}

	private static String readFileAsString(String fileName) throws IOException {
		String data = "";
		data = new String(Files.readAllBytes(Paths.get(fileName)));
		return data;
	}

	private static String encodeUnicode(String str, int key) {
		if (str.length() == 0) {
			return "";
		}
		for (int i = 0; i < str.length(); i++) {
			sb.append((char) (str.charAt(i) + key));
		}
		return sb.toString();
	}

	private static String decodeUnicode(String str, int key) {
		if (str.length() == 0) {
			return "";
		}
		for (int i = 0; i < str.length(); i++) {
			sb.append((char) (str.charAt(i) - key));
		}
		return sb.toString();
	}

	private static String encodeShift(String str, int key) {
		if (str.length() == 0) {
			return "";
		}
		for (int i = 0; i < str.length(); i++) {
			sb.append(shiftEncode(str.charAt(i), key));
		}
		return sb.toString();
	}

	private static String decodeShift(String str, int key) {
		if (str.length() == 0) {
			return "";
		}
		for (int i = 0; i < str.length(); i++) {
			sb.append(shiftDecode(str.charAt(i), key));
		}
		return sb.toString();
	}

	private static int getIndex(char c) {
		if (Character.isWhitespace(c)) {
			return -1;
		}
		if (Character.isUpperCase(c)) {
			c = Character.toLowerCase(c);
		}
		for (int i = 0; i < alphabet.length(); i++) {
			if (alphabet.charAt(i) == c) {
				return i;
			}
		}
		return -1;
	}

	private static char shiftEncode(char c, int k) {
		if (getIndex(c) == -1) {
			return c;
		}
		int ind = getIndex(c) + k;
		if (ind >= alphabet.length()) {
			ind = ind % alphabet.length();
		}
		return Character.isUpperCase(c) ? Character.toUpperCase(alphabet.charAt(ind)) : alphabet.charAt(ind);
	}

	private static char shiftDecode(char c, int k) {
		if (getIndex(c) == -1) {
			return c;
		}
		int ind = getIndex(c) - k;
		if (ind < 0) {
			ind = (alphabet.length()) - (k - getIndex(c));
		}
		return Character.isUpperCase(c) ? Character.toUpperCase(alphabet.charAt(ind)) : alphabet.charAt(ind);
	}
}
