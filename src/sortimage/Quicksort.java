package sortimage;

public class Quicksort {
	static void swap(int[] arr, int i, int j) {
		int k = arr[i];
		arr[i] = arr[j];
		arr[j] = k;
	}
	
	public static int partition(int[] arr, int start, int end) {
		int pivotIndex = start;
		int pivotValue = arr[pivotIndex];
		
		int i = start;
		for(int j = start + 1; j <= end; j++) {
			if(arr[j] <= pivotValue) {
				i++;
				swap(arr, i, j);
			}
		}
		
		swap(arr, i, pivotIndex);
		
		return i;
	}
	
	public static void quicksort(int[] arr, int start, int end) {
		if(start >= end) return;
		
		int index = partition(arr, start, end);
		
		quicksort(arr, start, index - 1);
		quicksort(arr, index + 1, end);
	}
	
	public static void main(String[] args) {
		int[] ints = new int[] {4, 7, 2, 1, 8, 6, 3, 5};
		for (int i = 0; i < ints.length; i++) {
			System.out.print(ints[i] + " ");
		}
		System.out.println();
		quicksort(ints, 0, ints.length - 1);
		for (int i = 0; i < ints.length; i++) {
			System.out.print(ints[i] + " ");
		}
		System.out.println();
		
	}
}
