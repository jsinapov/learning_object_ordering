package utils;

import java.util.ArrayList;
import java.util.Random;

public class PermutationGenerator {

    private int[] perm;
    private static Random r = new Random();
    
    ArrayList<int[]> permutations;

    public PermutationGenerator() {
       
        permutations = new ArrayList<int[]>();
    }
    
    public void computePermutations(int [] a, int n){
    	  if (n == 1) {
    		  
    		  int [] a_copy = new int[a.length];
    		  for (int i = 0; i < a.length; i++){
    			  a_copy[i]=a[i];
    		  }
    		  permutations.add(a_copy);
    		  
              return;
          }
          for (int i = 0; i < n; i++) {
              swap(a, i, n-1);
              computePermutations(a, n-1);
              swap(a, i, n-1);
          }
    }
    
    public ArrayList<int[]> retrievePermutations(){
    	return permutations;
    }
    
    public void reset(){
        permutations = new ArrayList<int[]>();
    }
    
    // swap the characters at indices i and j
    public void swap(int[] a, int i, int j) {
        int c = a[i];
        a[i] = a[j];
        a[j] = c;
    }

    
    public void perm2(char[] a, int n) {
        if (n == 1) {
            System.out.println(a);
            return;
        }
        for (int i = 0; i < n; i++) {
            swap(a, i, n-1);
            perm2(a, n-1);
            swap(a, i, n-1);
        }
    }  

    // swap the characters at indices i and j
    public void swap(char[] a, int i, int j) {
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }


    /*public int[] next() {
        helper(perm, perm.length);
        return perm;
    }

    private static void helper(int[] a, int length) {
        if (length > 1) {
            swap(a, r.nextInt(length), length - 1);
            helper(a, length - 1);
        }
    }

    private static void swap(int a[], int i, int j) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
    }*/

    /*public static void main(String[] args) {
        PermutationGenerator generator = new PermutationGenerator(4);
        int[] perm = generator.next();
        for (int i = 0; i < perm.length; i++) {
            System.out.println(perm[i]);
        }
    }*/
}