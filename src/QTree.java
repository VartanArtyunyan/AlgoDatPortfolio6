// Vartan Artyunyan
// Martikelnummer 5120007

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

public class QTree {

	int tolleranz = 0;

	public QTree(int tolleranz) {
		this.tolleranz = tolleranz;
	}

	public Knoten createTree(String fname) {
		Knoten root = new Knoten();
		int größe = 0;
		int[][] bild = new int[1024][1024];

		try (InputStream inputStream = new FileInputStream(fname);) {
			BufferedImage image = ImageIO.read(inputStream);

			größe = image.getWidth();
			int[] pixel = image.getRGB(0, 0, größe, größe, null, 0, größe);

			for (int i = 0; i < pixel.length; i++) {
				bild[i % größe][i / größe] = pixel[i];
			}

		} catch (IOException ex) {
			ex.printStackTrace();
		}

		createChildren(root, bild);
		determineValue(root);

		return root;
	}

	public BufferedImage showLayer(Knoten root, int ebene) {
		int size = 1024;
		Color[][] pixels = new Color[size][size];

		pixels = Knoten.combine(root.getPixels(size, ebene));

		BufferedImage bi = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);

		for (int j = 0; j < size; j++) {
			for (int i = 0; i < size; i++) {
				if (pixels[j][i] != null)
					bi.setRGB(j, i, pixels[j][i].getRGB());
			}
		}

		return bi;
	}

	public void createChildren(Knoten knoten, int[][] bild) {
		int länge = bild.length;
		if (länge == 2) {
			for (int i = 0; i < 4; i++) {
				knoten.kinder[i] = new Knoten(bild[i % 2][i / 2]);
			}
		} else {
			int subPxl[][][] = getSubPxls(bild);
			for (int i = 0; i < 4; i++) {
				Knoten neuesKind = new Knoten();
				createChildren(neuesKind, subPxl[i]);
				determineValue(neuesKind);
				knoten.kinder[i] = neuesKind;
			}

		}
	}

	public int[][][] getSubPxls(int[][] bild) {
		int newSize = bild.length / 2;
		int output[][][] = new int[4][newSize][newSize];

		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < newSize; i++) {
				for (int j = 0; j < newSize; j++) {
					output[k][i][j] = bild[i + ((k % 2) * newSize)][j + ((k / 2) * newSize)];
				}
			}
		}

		return output;
	}

	public void determineValue(Knoten knoten) {
		if (knoten.hasChildren()) {
			int temp = 0;
			for (int i = 0; i < knoten.kinder.length; i++) {
				temp = temp + knoten.kinder[i].r;
			}
			knoten.r = temp / 4;
			temp = 0;
			for (int i = 0; i < knoten.kinder.length; i++) {
				temp = temp + knoten.kinder[i].g;
			}
			knoten.g = temp / 4;
			temp = 0;
			for (int i = 0; i < knoten.kinder.length; i++) {
				temp = temp + knoten.kinder[i].b;
			}
			knoten.b = temp / 4;
		}

		if (knoten.childrenHaveSameColor(tolleranz)) {
			for (int i = 0; i < 4; i++) {
				knoten.kinder[i] = null;
			}
		}
	}

}

class Knoten {

	int r;
	int g;
	int b;
	Knoten kinder[];

	public boolean hasChildren() {
		boolean output = true;
		for (int i = 0; i < kinder.length; i++) {
			if (kinder[i] == null)
				output = false;
		}
		return output;
	}

	public boolean childrenHaveChildren() {
		boolean output = true;

		for (int i = 0; i < 4; i++) {
			if (!kinder[i].hasChildren())
				output = false;
		}
		return output;
	}

	public boolean childrenHaveSameColor(int tolleranz) {
		boolean output = true;
		if (childrenHaveChildren()) {
			for (int i = 0; i < 4; i++) {
				if (!kinder[i].childrenHaveSameColor(tolleranz))
					output = false;
			}
			return output;
		} else {

			for (int i = 0; i < 3; i++) {
				if (!colorEquals(kinder[i].getColor(), kinder[i + 1].getColor(), tolleranz))
					output = false;
			}
			return output;
		}
	}

	public boolean colorEquals(Color c1, Color c2, int tolleranz) {
		boolean output = true;

		int r1 = c1.getRed();
		int g1 = c1.getGreen();
		int b1 = c1.getBlue();

		int r2 = c2.getRed();
		int g2 = c2.getGreen();
		int b2 = c2.getBlue();

		output =r1 - r2 < tolleranz && r1 - r2 > 0 - tolleranz && 
				g1 - g2 < tolleranz && g1 - g2 > 0 - tolleranz && 
				b1 - b2 < tolleranz && b1 - b2 > 0 - tolleranz;
		return output;
	}

	public static Color[][] combine(Color[][][] input) {
		int size = input[0].length;
		int newSize = size * 2;
		Color output[][] = new Color[newSize][newSize];

		for (int k = 0; k < 4; k++) {
			for (int i = 0; i < size; i++) {
				for (int j = 0; j < size; j++) {
					output[i + ((k % 2) * size)][j + ((k / 2) * size)] = input[k][i][j];
				}
			}
		}
		return output;
	}

	public Color[][][] getPixels(int res, int depth) {
		Color[][][] output = new Color[4][res / 2][res / 2];
		if (depth < 1) {
			for (int k = 0; k < 4; k++) {
				for (int i = 0; i < output[k].length; i++) {
					for (int j = 0; j < output[k].length; j++) {
						output[k][i][j] = getColor();
					}
				}
			}
		} else if (childrenHaveChildren()) {
			for (int i = 0; i < 4; i++) {
				output[i] = combine(kinder[i].getPixels(res / 2, depth - 1));
			}
		} else {
			for (int k = 0; k < 4; k++) {
				for (int i = 0; i < res / 2; i++) {
					for (int j = 0; j < res / 2; j++) {
						output[k][i][j] = kinder[k].getColor();
					}
				}
			}
		}

		return output;
	}

	public void setRGB(int wert) {
		b = (wert) & 0xFF;
		g = (wert >> 8) & 0xFF;
		r = (wert >> 16) & 0xFF;
	}

	public Color getColor() {
		return new Color(r, g, b);
	}

	public Knoten() {
		r = 0;
		g = 0;
		b = 0;
		kinder = new Knoten[4];
	}

	public Knoten(int wert) {
		setRGB(wert);
		kinder = new Knoten[4];
	}

}