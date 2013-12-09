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
import notes.NoteTrainer;
import utils.FileUtils;

public class HymnDatabase {

	private static final Character DATABASE_FILE_FIELD_DELMIITER = ';';

	private static final boolean DEBUG = true;

	private final String databaseDir;
	private Map<String, NoteSequence> database;
	private static Map<String, HymnDatabase> databases = new HashMap<>();

	public static HymnDatabase getDatabase(String databaseDir) {
		if (!databases.containsKey(databaseDir)) {
			HymnDatabase database = new HymnDatabase(databaseDir);
			databases.put(databaseDir, database);
		}

		return databases.get(databaseDir);
	}

	private HymnDatabase(String databaseDir) {
		this.databaseDir = databaseDir;
		database = new HashMap<>();
	}

	private HymnDatabase() {
		this.databaseDir = "";
		database = new HashMap<>();
	}

	public void initializeDatabase(NoteTrainer trainer) {
		database.clear();

		File[] hymnFiles = FileUtils.getWavFiles(databaseDir);

		for (File hymnFile : hymnFiles) {
			String hymnName = hymnFile.getName();
			NoteSequence sequence = NoteSequence.generateSequenceForWavFile(
					hymnFile.getAbsolutePath(), trainer);

			if (DEBUG) {
				System.out.println("Loaded hymn " + hymnName + ", sequence: "
						+ sequence);
			}
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

	public String matchHymn(String sampleFilePath, NoteTrainer trainer) {
		if (DEBUG) {
			System.out.format("Generating sequence for sample file %s...\n",
					sampleFilePath);
		}
		NoteSequence sampleSequence = NoteSequence.generateSequenceForWavFile(
				sampleFilePath, trainer);

		int minSimilarity = Integer.MAX_VALUE;
		String closestHymnName = null;

		for (Entry<String, NoteSequence> entry : database.entrySet()) {
			String currentHymnName = entry.getKey();
			NoteSequence currentSequence = entry.getValue();

			if (DEBUG) {
				System.out.format(
						"Computing similarity between sample and hymn '%s': ",
						currentHymnName);
			}
			int similarity = currentSequence.distanceTo(sampleSequence);

			if (DEBUG) {
				System.out.println(similarity);
			}

			if (minSimilarity > similarity) {
				minSimilarity = similarity;
				closestHymnName = currentHymnName;
			}
		}

		if (DEBUG) {
			if (closestHymnName != null) {
				System.out.format("Closest hymn was '%s' with score %d\n",
						closestHymnName, minSimilarity);
			} else {
				System.out.println("No hymn found!");
			}
		}

		return closestHymnName;
	}

	private static HymnDatabase fileBasedDatabase = null;
	private static NoteTrainer fileBasedTrainer = null;

	public static void initializeDatabase(String databaseFilePath,
			String trainerFilePath) {
		fileBasedDatabase = new HymnDatabase();
		fileBasedTrainer = new NoteTrainer();

		fileBasedDatabase.loadDatabaseFromFile(databaseFilePath);
		fileBasedTrainer.loadTrainerFromFile(trainerFilePath);
	}

	public static String matchHymn(String sampleFilePath) {
		if (fileBasedDatabase == null || fileBasedTrainer == null) {
			return null;
		}

		return fileBasedDatabase.matchHymn(sampleFilePath, fileBasedTrainer);
	}

	public static void main(String... args) {
		String databaseFilePath = "/Users/jandersen/Dropbox/Classes/Senior/Semester 2/CS 450/Hymn Learner/database.txt";
		String trainerFilePath = "/Users/jandersen/Dropbox/Classes/Senior/Semester 2/CS 450/Hymn Learner/learner.txt";
		String sampleFilePath = "/Users/jandersen/Desktop/test-hymn327.wav";

		initializeDatabase(databaseFilePath, trainerFilePath);
		System.out.println(matchHymn(sampleFilePath));
	}
}
