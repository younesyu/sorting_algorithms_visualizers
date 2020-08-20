package sortimage;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageSelectionSorter extends PApplet {
	PImage image;
	int h, w;
	int gcd;
	Cluster[][] clusters;
	int nbClustersLine;
	int nbClustersColumn;

	class Cluster {
		int id;
		int x;
		int y;
		int[][] pixels;

		public Cluster(int id, int x, int y, int[][] pixels) {
			this.pixels = pixels;
			this.y = y;
			this.x = x;
			this.id = id;
		}
	}

	int speed = 20;

	public void settings() {
		image = loadImage("hagia.jpg");
		h = image.height;
		w = image.width;
		size(w, h);

		gcd = gcd(h, w)/4;
		nbClustersLine = w / gcd;
		nbClustersColumn = h / gcd;

		int[][] pixelArray = new int[w][h];
		image.loadPixels();
		for (int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int loc = i + j * w;
				pixelArray[i][j] = image.pixels[loc];
			}
		}

		clusters = new Cluster[nbClustersLine][nbClustersColumn];

		for (int i = 0; i < nbClustersLine; i++) {
			for (int j = 0; j < nbClustersColumn; j++) {
				int[][] pixels = new int[gcd][gcd];
				for (int x = 0; x < gcd; x++) {
					for (int y = 0; y < gcd; y++) {
						pixels[x][y] = pixelArray[i * gcd + x][j * gcd + y];
					}
				}
				clusters[i][j] = new Cluster(i + j * nbClustersLine, i, j, pixels);
			}
		}
		
		xlast = nbClustersLine - 1;
		ylast = nbClustersColumn - 1;

		shuffle();
	}
	
	int xcur = 0, ycur = 0;
	int xmin = 0, ymin = 0;
	int xlast, ylast;

	public void draw() {
		
		for(int i = 0; i < speed; i++) {
			if (xlast == 0 && ylast == 0) {
				noLoop();
				break;
			}
		
			if(clusters[xcur][ycur].id > clusters[xmin][ymin].id) {
				xmin = xcur; ymin = ycur;
			}
			
			try {
				int[] nextCoor = incrementCoordinates(xcur, ycur, nbClustersLine, nbClustersColumn);
				int nextx = nextCoor[0], nexty = nextCoor[1];
				
				if(clusters[xcur][ycur].id == clusters[xlast][ylast].id) {
					swap(clusters, xmin, ymin, xlast, ylast);
					
					int[] prevCoor = decrementCoordinates(xlast, ylast, nbClustersLine, nbClustersColumn);
					xlast = prevCoor[0];
					ylast = prevCoor[1];

					xcur = 0; ycur = 0;
					xmin = 0; ymin = 0;
				} else {
					xcur = nextx; ycur = nexty;
				}
			} catch (Exception e) {
				e.printStackTrace();
				noLoop();
				break;
			}	
		}
		
		loadPixels();

		for (int i = 0; i < nbClustersLine; i++) {
			for (int j = 0; j < nbClustersColumn; j++) {
				for (int x = 0; x < gcd; x++) {
					for (int y = 0; y < gcd; y++) {
						int loc = (i * gcd + x) + (j * gcd + y) * w;
						pixels[loc] = clusters[i][j].pixels[x][y];
					}
				}
			}
		}
		updatePixels();
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ImageSelectionSorter mySketch = new ImageSelectionSorter();
		PApplet.runSketch(processingArgs, mySketch);
	}

	private int gcd(int number1, int number2) {
		if (number2 == 0) {
			return number1;
		}
		return gcd(number2, number1 % number2);
	}
	

	void shuffle() {
		Random random = new Random();

		for (int i = 0; i < nbClustersLine; i++) {
			for (int j = 0; j < nbClustersColumn; j++) {
				int randomI = random.nextInt(nbClustersLine - i) + i;
				int randomJ = random.nextInt(nbClustersColumn - j) + j;

				swap(clusters, i, j, randomI, randomJ);

			}
		}
	}

	void swap(Object[][] array, int i, int j, int newI, int newJ) {
		Object k = array[i][j];
		array[i][j] = array[newI][newJ];
		array[newI][newJ] = k;
	}
	
	int[] decrementCoordinates(int x, int y, int xbor, int ybor) throws Exception {
		if(x == 0) {
			if(y == 0) {
				throw new Exception("Débordement");
			} else {
				x = xbor - 1;
				y--;
			}
		} else {
			x--;
		}
		
		return new int[]{x,y};
	}
	
	int[] incrementCoordinates(int x, int y, int xbor, int ybor) throws Exception {
		if(x == xbor - 1) {
			if(y == ybor - 1) {
				x = 0; y = 0;
			} else {
				x = 0;
				y++;
			}
		} else {
			x++;
		}
		
		return new int[]{x,y};
	}
}

