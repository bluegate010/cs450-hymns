package editdistance;

import java.util.ArrayList;

public class EditDistanceCalculator {

	private static final boolean PRINT_GRID = false;

	private static final int DEFAULT_INDEL_COST = 1;
	private static final int DEFAULT_SUB_COST = 2;
	private static final int DEFAULT_MATCH_COST = 0;
	private static final boolean DEFAULT_IGNORE_EDGE_INDEL = true;

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

		int[] curr = new int[a.size() + 1];

		if (PRINT_GRID) {
			System.out.print("     ");
			for (Object o : a) {
				System.out.print(o + "  ");
			}
			System.out.println("");
		}

		for (int row = 1; row < b.size() + 1; row++) {
			curr = new int[a.size() + 1];

			// Set the first cell to be the indel cost, so we don't have to
			// do bounds checking later.
			curr[0] = row * indelCost;

			int currInsertCost, currDeleteCost, currMatchCost;

			for (int col = 1; col < a.size() + 1; col++) {
				currMatchCost = prev[col - 1]
						+ (a.get(col - 1).equals(b.get(row - 1)) ? matchCost
								: subCost);
				currDeleteCost = prev[col] + indelCost;
				currInsertCost = curr[col - 1] + indelCost;

				if (ignoreEdgeIndel && row == b.size()) {
					currInsertCost -= indelCost;
				}

				curr[col] = Math.min(currInsertCost,
						Math.min(currDeleteCost, currMatchCost));
			}

			if (PRINT_GRID) {
				if (row > 1) {
					System.out.print(b.get(row - 2) + " ");
				} else {
					System.out.print("  ");
				}
				for (int i : prev) {
					System.out.print(i + ", ");
				}
				System.out.println("");
			}

			prev = curr;
		}

		if (PRINT_GRID) {
			System.out.print(b.get(b.size() - 1) + " ");
			for (int i : curr) {
				System.out.print(i + ", ");
			}
			System.out.println("");
		}

		return curr[a.size()];
	}
}
