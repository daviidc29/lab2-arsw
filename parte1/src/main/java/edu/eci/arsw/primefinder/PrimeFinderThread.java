package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

public class PrimeFinderThread extends Thread{

	
	int a,b,n;
	
	private List<Integer> primes=new LinkedList<Integer>();
	
	public PrimeFinderThread(int n,int a, int b) {
		super();
		this.n = n;
		this.a = a;
		this.b = b;
	}

	public void run(){
		int total = b-a;
		int segmento = total/n;
		for (int i=a+1;i<=segmento;i++){						
			if (isPrime(i)){
				primes.add(i);
				System.out.println(i);
			}
		}
		
		
	}
	
	boolean isPrime(int n) {
	    if (n%2==0) return false;
	    for(int i=3;i*i<=n;i+=2) {
	        if(n%i==0)
	            return false;
	    }
	    return true;
	}

	public List<Integer> getPrimes() {
		return primes;
	}
	
	
	
	
}
