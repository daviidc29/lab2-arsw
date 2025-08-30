package edu.eci.arsw.primefinder;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		List<Integer> primes = PrimeFinderThread.findPrimes(5, 0, 40000000);
		System.out.println("Primos: " + primes);
		System.out.println("Total primos: " + primes.size());

	}
	
}