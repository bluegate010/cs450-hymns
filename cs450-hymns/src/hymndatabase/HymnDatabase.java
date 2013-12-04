package hymndatabase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import notes.NoteSequence;
import utils.FileUtils;

public class HymnDatabase {

	private static final Character DATABASE_FILE_FIELD_DELMIITER = ';';

	public static final String DEFAULT_HYMN_DATABSE_DIR = "audio/hymns/";

	private Map<String, NoteSequence> database;
	private static Map<String, HymnDatabase> databases = new HashMap<>();

	public static void loadDefaultDatabase() {
		// Just call getDatabase() to get it loaded.
		getDefaultDatabase();
	}

	public static HymnDatabase getDefaultDatabase() {
		return getDatabase(DEFAULT_HYMN_DATABSE_DIR);
	}

	public static HymnDatabase getDatabase(String databaseDir) {
		if (!databases.containsKey(databaseDir)) {
			HymnDatabase database = new HymnDatabase(databaseDir);
			databases.put(databaseDir, database);
		}

		return databases.get(databaseDir);
	}

	private HymnDatabase(String databaseDir) {
		database = new HashMap<>();
		loadDatabase(databaseDir);
	}

	private void loadDatabase(String databaseDir) {
		database.clear();

		File[] hymnFiles = FileUtils.getWavFiles(databaseDir);

		for (File hymnFile : hymnFiles) {
			String hymnName = hymnFile.getName();
			NoteSequence sequence = NoteSequence
					.generateSequenceForWavFile(hymnFile.getAbsolutePath());

			database.put(hymnName, sequence);
		}
	}

	public boolean saveDatabase(String outputFilePath) {
		try (PrintWriter writer = new PrintWriter(outputFilePath, "UTF-8")) {
			for (Entry<String, NoteSequence> entry : database.entrySet()) {
				writer.println(entry.getKey() + DATABASE_FILE_FIELD_DELMIITER
						+ entry.getValue());
			}
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean loadDatabaseFromFile(String inputFilePath) {
		database.clear();

		try (FileInputStream fstream = new FileInputStream(inputFilePath);
				BufferedReader br = new BufferedReader(new InputStreamReader(
						fstream))) {
			String strLine;
			while ((strLine = br.readLine()) != null) {
				String[] pieces = strLine.split(Character
						.toString(DATABASE_FILE_FIELD_DELMIITER));

				if (pieces.length != 2) {
					continue;
				}

				String hymnName = pieces[0];
				NoteSequence sequence = NoteSequence.fromString(pieces[1]);

				database.put(hymnName, sequence);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public String matchHymn(String sampleFilePath) {
		NoteSequence sampleSequence = NoteSequence
				.generateSequenceForWavFile(sampleFilePath);

		int minSimilarity = Integer.MAX_VALUE;
		String closestHymnName = null;

		for (Entry<String, NoteSequence> entry : database.entrySet()) {
			String currentHymnName = entry.getKey();
			NoteSequence currentSequence = entry.getValue();

			int similarity = currentSequence.distanceTo(sampleSequence);
			if (minSimilarity > similarity) {
				minSimilarity = similarity;
				closestHymnName = currentHymnName;
			}
		}

		return closestHymnName;
	}
}
