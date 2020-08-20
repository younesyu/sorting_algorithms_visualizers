package sortimage;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class Sketch extends PApplet {
	PImage tebboune;
	int[][] doubleArray;
	Pixid[] tebbounePixels;
	Pixid[] imgPixels;
	Cluster[] clusters;
	int gcd;
	int h = 410;
	int w = 730;

	class Pixid {
		int pixel;
		int id;

		public Pixid(int p, int i) {
			pixel = p;
			id = i;
		}
	}
	
	class Cluster {
		int[][] pixels;
		int id;
		
		public Cluster(int[][] p, int i) {
			pixels = p;
			id = i;
		}
	}
	
	private int gcd(int number1, int number2) {
        //base case
        if(number2 == 0){
            return number1;
        }
        return gcd(number2, number1%number2);
    }

	public void settings() {
		size(w, h);
		tebboune = loadImage("tebboune.jpg");
		imgPixels = new Pixid[w * h];
		tebbounePixels = new Pixid[w * h];
		doubleArray = new int[w][h];

		tebboune.loadPixels();
		
		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				doubleArray[i][j] = tebboune.pixels[i + j*w];
			}
		}
		
		gcd = gcd(w, h);
		clusters = new Cluster[(w*h)/(gcd*gcd)];
		
		for(int i = 0; i < w; i += gcd) {
			for(int j = 0; j < h; j += gcd) {
				int[][] pixels = new int[gcd][gcd];
				for(int k = 0; k < gcd; k++) {
					for(int l = 0; l < gcd; l++) {
						pixels[k][l] = doubleArray[i + k][j + l];
					}
				}
				int id = (i/gcd) + (j / gcd) * (w/gcd);
				clusters[id] = new Cluster(pixels, id); 
			}
		}
		
		
		for(int x = 0; x < tebboune.pixels.length; x++) {
			imgPixels[x] = new Pixid(tebboune.pixels[x], x);
			tebbounePixels[x] = new Pixid(tebboune.pixels[x], x);
		}

		shuffle2();
//		sort(imgPixels);
	}
	
	private void shuffle2() {
		Random random = new Random();
		for(int i = 0; i < clusters.length; i++) {
			int randomIndex = random.nextInt(clusters.length - i) + i;
			Cluster k = clusters[i];
			clusters[i] = clusters[randomIndex];
			clusters[randomIndex] = k;
		}
		
	}

	private void shuffle() {
		Random random = new Random();

		for (int i = 0; i < imgPixels.length; i++) {
			int randomIndex = random.nextInt(imgPixels.length - i) + i;
			Pixid k = imgPixels[i];
			imgPixels[i] = imgPixels[randomIndex];
			imgPixels[randomIndex] = k;
		}
	}

	private void sort(Pixid[] pixids) {
		List<Pixid> list = Arrays.asList(pixids);
		list.sort((Pixid p1, Pixid p2) -> p1.id - p2.id);
		pixids = (Pixid[]) list.toArray();
	}

	int i = 0;
	
	public void draw() {
//		
//		for(int m = 0; m < 5000000; m++) {
//			Pixid a = imgPixels[i];
//			Pixid b = imgPixels[i + 1];
//	
//			if (a.id > b.id) {
//				Pixid k = imgPixels[i];
//				imgPixels[i] = imgPixels[i + 1];
//				imgPixels[i + 1] = k;
//				
//			}
//			
//			if(i < imgPixels.length - 2) {
//				i++;
//			} else {
//				i = 0;
//			}
//		}

		loadPixels();

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				int loc = x + y * width;
				//pixels[loc] = imgPixels[loc].pixel;
				//pixels[loc] = doubleArray[x][y];
				int clusterNb = (loc / (gcd*gcd));
				int i = x % (clusterNb*gcd+1);
				int j = y % (clusterNb*gcd+1);
				pixels[loc] = clusters[clusterNb].pixels[i-1][j-1];
			}
		}
		updatePixels();
		// System.out.println("err");
		if (equals(imgPixels, tebbounePixels)) {
			noLoop();
			System.out.println("done");
		}
	}

	private boolean equals(Pixid[] a, Pixid[] b) {
		for(int i = 0; i < a.length; i++) {
			if (a[i].id != b[i].id) return false;
		}
		return true;
	}

	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		Sketch mySketch = new Sketch();
		PApplet.runSketch(processingArgs, mySketch);
	}
}
