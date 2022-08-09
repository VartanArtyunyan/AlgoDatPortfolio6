// Vartan Artyunyan
// Martikelnummer 5120007

public class Bergsteiger {

	int[][] ebene = new int[][] { { 0, 1, 0, 0, 3 }, { 0, 0, 0, 1, 0 }, { 0, 1, 0, 0, 0 }, { 0, 0, 0, 1, 0 },
			{ 2, 1, 0, 0, 0 } };

	int[] position;

	int[] ziel;

	public Bergsteiger() {
		position = new int[] { 4, 0 };
		ziel = new int[] { 0, 4 };
	}

	public void findPath() {

		while (!amZiel()) {
			int[] oldPos = position;
			position = searchNearest(getNachbarn(position));
			System.out.println("[" + oldPos[0] + "|" + oldPos[1] + "] -> [" + position[0] + "|" + position[1] + "]");

		}
	}

	public boolean amZiel() {
		return position[0] == ziel[0] && position[1] == ziel[1];
	}

	public int[][] getNachbarn(int[] position) {

		int[][] output = new int[4][2];

		int[] up = new int[] { position[0] + 1, position[1] };

		int[] right = new int[] { position[0], position[1] + 1 };

		int[] down = new int[] { position[0] - 1, position[1] };

		int[] left = new int[] { position[0], position[1] - 1 };

		if (posIsLegit(up))
			output[0] = up;
		else
			output[0] = null;
		if (posIsLegit(right))
			output[1] = right;
		else
			output[1] = null;
		if (posIsLegit(down))
			output[2] = down;
		else
			output[2] = null;
		if (posIsLegit(left))
			output[3] = left;
		else
			output[3] = null;

		return output;
	}

	public int[] searchNearest(int[][] positions) {
		int nearest = -1;
		double nearestDist = Double.MAX_VALUE;

		for (int i = 0; i < positions.length; i++) {
			if (positions[i] != null) {
				double dist = calcDist(positions[i], ziel);
				if (dist < nearestDist) {
					nearestDist = dist;
					nearest = i;

				}

			}
		}

		return positions[nearest];
	}

	public double calcDist(int[] pos1, int[] pos2) {
		return Math.sqrt(Math.pow((pos1[0] - pos2[0]), 2) + Math.pow((pos1[1] - pos2[1]), 2));
	}

	public boolean posIsLegit(int[] pos) {
		boolean output = false;

		if (pos[0] >= 0 && pos[0] < ebene.length && pos[1] >= 0 && pos[1] < ebene.length && ebene[pos[0]][pos[1]] != 1)
			output = true;

		return output;
	}

}
