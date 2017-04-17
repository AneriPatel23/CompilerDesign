package com.compiler;

import java.io.BufferedReader; //package reads text from a character-input stream
import java.io.FileReader; //package reads character files
import java.io.IOException; //package handles exceptions in I/O

// reads a text file
public class readFile {
	public String readFile(String fileName) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(fileName));
		try {
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append("\n");
				line = br.readLine();
			}
			return sb.toString();
		} finally {
			br.close();
		}
	}
}
