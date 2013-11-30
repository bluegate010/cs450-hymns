package test;

import editdistance.EditDistanceCalculator;

public class EditDistance {

	public static void main(String[] args) {
		EditDistanceCalculator c = new EditDistanceCalculator();
		c.setIndelCost(1);
		c.setMatchCost(0);
		c.setSubCost(2);
		c.setIgnoreEdgeIndel(true);
		printDistance(c, "foobarr", "oba");
	}

	private static void printDistance(EditDistanceCalculator c, String s1,
			String s2) {
		System.out.println(c.computeAlignment(stringToObjArray(s1),
				stringToObjArray(s2)));
	}

	private static Object[] stringToObjArray(String s) {
		Object[] arr = new Object[s.length()];

		for (int i = 0; i < s.length(); i++) {
			arr[i] = new Character(s.charAt(i));
		}

		return arr;
	}

}
