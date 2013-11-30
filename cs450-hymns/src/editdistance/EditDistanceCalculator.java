package editdistance;

import java.util.ArrayList;

public class EditDistanceCalculator {

	private static final int DEFAULT_INDEL_COST = 5;
	private static final int DEFAULT_SUB_COST = 1;
	private static final int DEFAULT_MATCH_COST = -3;
	private static final boolean DEFAULT_IGNORE_EDGE_INDEL = false;

	private int indelCost;
	private int subCost;
	private int matchCost;
	private boolean ignoreEdgeIndel;

	public EditDistanceCalculator() {
		indelCost = DEFAULT_INDEL_COST;
		subCost = DEFAULT_SUB_COST;
		matchCost = DEFAULT_MATCH_COST;
		ignoreEdgeIndel = DEFAULT_IGNORE_EDGE_INDEL;
	}

	public void setIndelCost(int indelCost) {
		this.indelCost = indelCost;
	}

	public void setSubCost(int subCost) {
		this.subCost = subCost;
	}

	public void setMatchCost(int matchCost) {
		this.matchCost = matchCost;
	}

	public void setIgnoreEdgeIndel(boolean ignoreEdgeIndel) {
		this.ignoreEdgeIndel = ignoreEdgeIndel;
	}

	public <T> int computeAlignment(ArrayList<? extends T> a,
			ArrayList<? extends T> b) {
		if (ignoreEdgeIndel && a.size() < b.size()) {
			// A should be the longest sequence if we're ignoring
			// edge indels.
			ArrayList<? extends T> tmp = a;
			a = b;
			b = tmp;
		}

		int[] prev = new int[a.size() + 1];

		// Initialize prev with the first column of indel costs
		for (int i = 0; i < a.size() + 1; i++) {
			prev[i] = i * (ignoreEdgeIndel ? matchCost : indelCost);
		}

		// Initialize curr as an empty array, just to make the compiler happy.
		int[] curr = new int[0];

		// System.out.print("     ");
		// for (Object o : a) {
		// System.out.print(o + "  ");
		// }
		// System.out.println("");

		for (int col = 1; col < b.size() + 1; col++) {
			curr = new int[a.size() + 1];

			// Set the first cell to be the indel cost, so we don't have to
			// do bounds checking later.
			curr[0] = col * indelCost;

			int currInsertCost, currDeleteCost, currMatchCost;

			for (int row = 1; row < a.size() + 1; row++) {
				currMatchCost = prev[row - 1]
						+ (a.get(row - 1).equals(b.get(col - 1)) ? matchCost
								: subCost);
				currDeleteCost = prev[row] + indelCost;
				currInsertCost = curr[row - 1] + indelCost;

				if (ignoreEdgeIndel && col == b.size()) {
					currInsertCost -= indelCost;
				}

				curr[row] = Math.min(currInsertCost,
						Math.min(currDeleteCost, currMatchCost));
			}

			// if (col > 1) {
			// System.out.print(b.get(col - 2) + " ");
			// } else {
			// System.out.print("  ");
			// }
			// for (int i : prev) {
			// System.out.print(i + ", ");
			// }
			// System.out.println("");

			prev = curr;
		}

		// System.out.print(b.get(b.size() - 1) + " ");
		// for (int i : curr) {
		// System.out.print(i + ", ");
		// }
		// System.out.println("");

		return curr[a.size()];
	}
}
