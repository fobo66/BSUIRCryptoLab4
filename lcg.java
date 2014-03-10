/* 	
LCG implementation
Iris Yuan
3/6/2014
*/

import java.util.Scanner;
import java.util.Random;
import java.lang.Math;

public class lcg {

	 public static Scanner scanner = new Scanner(System.in);
	 
	 // basic recursive GCD method for use when given x0,x1... compute m
	 public static int gcd(int number1, int number2) {
	        //base case
	        if (number2 == 0){
	            return number1;
	        }
	        return gcd(number2, number1 % number2);
	 }
	 
	// computeL() uses built-in random number generator to compute l-bit integer m
	// in 2^(l-1) < m < 2^l along with a, b, x0 (seed) values
	 
	// returns first n+1 elements x0, x1, xn in linear congruential sequence
	 public static int[] computeX(int l, int n) {
		 
		 int min = (int) Math.pow(2, (l-1));
		 int max = (int) Math.pow(2, l);
		 Random rand = new Random();
		 
		 // compute l-bit integer m
		 int m = rand.nextInt((min+1)) + max;	
		 
		 // compute random a, b, x0 in {0, ... m-1}
		 // max is m-1, min is 0
		 // rand.nextInt((m-1 - 0) + 1) + 0 = rand.nextInt(m)  
		 int a = rand.nextInt(m);
		 int b = rand.nextInt(m);
		 int x = rand.nextInt(m);
		 
		 int[] array = new int[n+1];
		 System.out.print("x values: ");
		 for (int i = 0; i < n+1; i ++) {
			 x = (a * x + b) % m;
			 System.out.println(x);
			 array[i] = x;
		 }
		 
		 return array;
	 }
	 
	 public static void main (String[] args)
     {
    	 // input positive integers l and n 
         System.out.print( "Input positive integer l: " );
         int l = scanner.nextInt();
         System.out.print( "Input positive integer n: " );
         int n = scanner.nextInt();
         
         // given a, b, and m, compute x0, x1, x2... 
         int[] array = computeX(l,n);

     } 
}