package sortimage;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import processing.core.PApplet;
import processing.core.PImage;

public class ImageParallelQuicksort extends PApplet {
	static ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);
	PImage image;
	int h, w;
	int gcd;
	static Cluster[] clusters;
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
		image = loadImage("falcon_img.jpg");
		h = image.height;
		w = image.width;
		size(w, h);
		
		gcd = gcd(h, w);
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
		
		clusters = new Cluster[nbClustersLine*nbClustersColumn];
		
		for(int i = 0; i < nbClustersLine; i++) {
			for(int j = 0; j < nbClustersColumn; j++) {
				int[][] pixels = new int[gcd][gcd];
				for(int x = 0; x < gcd; x++) {
					for(int y = 0; y < gcd; y++) {
						pixels[x][y] = pixelArray[i*gcd + x][j*gcd + y];
					}
				}
				clusters[i + j*nbClustersLine] = new Cluster(i + j*nbClustersLine, i, j, pixels);
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
				
				swap(clusters, i + j*nbClustersLine, randomI + randomJ * nbClustersLine);
				
			}
		}
	}
	
	
	static void swap(Object[] array, int i, int j) {
		Object k = array[i];
		array[i] = array[j];
		array[j] = k;
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static int partition(Cluster[] arr, int start, int end) {
		int pivotIndex = start;
		int pivotValue = arr[pivotIndex].id;
		
		int i = start;
		for(int j = start + 1; j <= end; j++) {
			if(arr[j].id <= pivotValue) {
				i++;
				swap(arr, i, j);
			}
		}
		
		swap(arr, i, pivotIndex);
		
		return i;
	}
	
	public static void quicksort(Cluster[] arr, int start, int end) {
		if(start >= end) return;
		
		int index = partition(arr, start, end);
		
		Runnable r1 = () -> quicksort(arr, start, index - 1);
		Runnable r2 = () -> quicksort(arr, index + 1, end);
		
		executor.execute(r1);
		executor.execute(r2);
	}
	
	boolean f = false;
	public void draw() {
		loadPixels();
		
		for(int i = 0; i < nbClustersLine; i++) {
			for(int j = 0; j < nbClustersColumn; j++) {
				for(int x = 0; x < gcd; x++) {
					for(int y = 0; y < gcd; y++) {
						int loc = (i*gcd + x) + (j*gcd + y)*w;
						pixels[loc] = clusters[i + j*nbClustersLine].pixels[x][y];
					}
				}
			}
		}
		updatePixels();
	}
	
	public static void main(String[] args) {
		String[] processingArgs = { "MySketch" };
		ImageParallelQuicksort mySketch = new ImageParallelQuicksort();
		PApplet.runSketch(processingArgs, mySketch);
		quicksort(ImageParallelQuicksort.clusters, 0, clusters.length - 1);
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
