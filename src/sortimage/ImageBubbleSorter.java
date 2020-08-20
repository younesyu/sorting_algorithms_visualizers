package sortimage;

import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageBubbleSorter extends PApplet {
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
	int speed = 10;
	public void settings() {
		image = loadImage("hagia.jpg");
		h = image.height;
		w = image.width;
		size(w, h);
		
		gcd = gcd(h, w)/4;
		nbClustersLine = w/gcd;
		nbClustersColumn = h/gcd;
		
		int[][] pixelArray = new int[w][h];
		image.loadPixels();
		for(int i = 0; i < w; i++) {
			for (int j = 0; j < h; j++) {
				int loc = i + j*w;
				pixelArray[i][j] = image.pixels[loc];
			}
		}
		
		clusters = new Cluster[nbClustersLine][nbClustersColumn];
		
		for(int i = 0; i < nbClustersLine; i++) {
			for(int j = 0; j < nbClustersColumn; j++) {
				int[][] pixels = new int[gcd][gcd];
				for(int x = 0; x < gcd; x++) {
					for(int y = 0; y < gcd; y++) {
						pixels[x][y] = pixelArray[i*gcd + x][j*gcd + y];
					}
				}
				clusters[i][j] = new Cluster(i + j*nbClustersLine, i, j, pixels);
			}
		}
		
		shuffle();
	}
	
	void shuffle() {
		Random random = new Random();
		
		for(int i = 0; i < nbClustersLine; i++) {
			for(int j = 0; j < nbClustersColumn; j++) {
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
	
	int sorterX = 0;
	int sorterY = 0;
	
	public void draw() {
		image.loadPixels();
	
		for(int m = 0; m < speed; m++) {
			int nextX = sorterX, nextY = sorterY;
			
			if(sorterX == nbClustersLine - 1) {
				nextX = 0;
				nextY = sorterY+1;
			} else {
				nextX = sorterX+1;
			}
			
			if(clusters[sorterX][sorterY].id > clusters[nextX][nextY].id) {
				swap(clusters, sorterX, sorterY, nextX, nextY);
			}
			
			sorterX = nextX;
			sorterY = nextY;
			 
			if(sorterY == nbClustersColumn -1 && sorterX == nbClustersLine -1) {
				sorterX= 0;
				sorterY= 0;
			}
		}
		
		
		
		loadPixels();
		
		for(int i = 0; i < nbClustersLine; i++) {
			for(int j = 0; j < nbClustersColumn; j++) {
				for(int x = 0; x < gcd; x++) {
					for(int y = 0; y < gcd; y++) {
						int loc = (i*gcd + x) + (j*gcd + y)*w;
						pixels[loc] = clusters[i][j].pixels[x][y];
					}
				}
			}
		}
		
		updatePixels();
	}
	
	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ImageBubbleSorter mySketch = new ImageBubbleSorter();
		PApplet.runSketch(processingArgs, mySketch);
	}
	
	private int gcd(int number1, int number2) {
        //base case
        if(number2 == 0){
        	System.out.println(number1);
            return number1;
        }
        return gcd(number2, number1%number2);
    }
}
