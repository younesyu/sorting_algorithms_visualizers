package sortimage;

public class SelectionSort {
	
	static void sort(int[] arr) {
		int n = arr.length;
		
        for (int i = 0; i < n-1; i++) { 
            // Find the minimum element in unsorted array 
            int min_idx = i; 
            for (int j = i+1; j < n; j++) 
                if (arr[j] < arr[min_idx]) 
                    min_idx = j;
            
			swap(arr, min_idx, i);
            
        }
	}
	
	static void swap(int[] arr, int i, int j) {
		int k = arr[i];
		arr[i] = arr[j];
		arr[j] = k;
	}
	
	public static void main(String[] args) {
		int[] arr = new int[] {1, 6, 5, 8, 9, 3};
		
		sort(arr);
		for (int i = 0; i < arr.length; i++) {
			System.out.print(" " + arr[i]);
		}
	}
	
}
