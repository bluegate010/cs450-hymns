package hymndatabase;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import notes.NoteSequence;
import utils.FileUtils;

public class HymnDatabase {

	public static final String DEFAULT_HYMN_DATABSE_DIR = "audio/hymns/";

	private Map<String, NoteSequence> database;
	private static Map<String, HymnDatabase> databases = new HashMap<>();

	public static HymnDatabase getDatabase() {
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
		File[] hymnFiles = FileUtils.getWavFiles(databaseDir);

		for (File hymnFile : hymnFiles) {
			String hymnName = hymnFile.getName();
			NoteSequence sequence = NoteSequence
					.generateSequenceForWavFile(hymnFile.getAbsolutePath());

			database.put(hymnName, sequence);
		}
	}

	public String matchHymn(String sampleFilePath) {
		NoteSequence sampleSequence = NoteSequence
				.generateSequenceForWavFile(sampleFilePath);

		int minSimilarity = Integer.MAX_VALUE;
		String closestHymnName = null;

		for (Entry<String, NoteSequence> entry : database.entrySet()) {
			String currentHymnName = entry.getKey();
			NoteSequence currentSequence = entry.getValue();

			int similarity = currentSequence.compareTo(sampleSequence);
			if (minSimilarity > similarity) {
				minSimilarity = similarity;
				closestHymnName = currentHymnName;
			}
		}

		return closestHymnName;
	}

}
