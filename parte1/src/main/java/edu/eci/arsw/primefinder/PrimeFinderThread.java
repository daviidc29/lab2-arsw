package edu.eci.arsw.primefinder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrimeFinderThread extends Thread {

    int a, b;

    // Lista propia de cada hilo sincronizada para leer tamaño con seguridad durante la pausa
    private final List<Integer> primes = Collections.synchronizedList(new ArrayList<>());

    // Control de pausa compartido por todos los hilos
    private static final Object PAUSE_LOCK = new Object();
    private static volatile boolean paused = false;

    public PrimeFinderThread(int a, int b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public void run() {
        for (int i = a; i <= b; i++) {
			if (paused) {
                synchronized (PAUSE_LOCK) {
                    while (paused) {
                        try {
                            PAUSE_LOCK.wait();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return; // salir si interrumpen el hilo
                        }
                    }
                }
            }

            if (isPrime(i)) {
                primes.add(i);
            }
        }
    }

    boolean isPrime(int n) {
        if (n < 2) return false;
        if (n == 2) return true;
        if (n % 2 == 0) return false;
        for (int d = 3; (long) d * d <= n; d += 2) {
            if (n % d == 0) return false;
        }
        return true;
    }

    public List<Integer> getPrimes() {
        return primes;
    }

    /**
     * Lanza n hilos, pausa a los 5 segundos, muestra conteo parcial, espera ENTER y reanuda
     */
    public static List<Integer> findPrimes(int n, int from, int to) {
        if (n <= 0) throw new IllegalArgumentException("n debe ser > 0");
        if (from > to) return new ArrayList<>();

        int total = to - from + 1;
        int base = total / n;
        int rem  = total % n;

        List<PrimeFinderThread> workers = new ArrayList<>(n);
        int start = from;
        for (int i = 0; i < n; i++) {
            int len = base + (i < rem ? 1 : 0);
            if (len <= 0) break;
            int end = start + len - 1;
            workers.add(new PrimeFinderThread(start, end));
            start = end + 1;
        }

        for (Thread t : workers) t.start();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        paused = true; 

        // Contar primos parciales listas sincronizadas 
        int parciales = 0;
        for (PrimeFinderThread t : workers) parciales += t.getPrimes().size();

        System.out.println("\nPAUSA:");
        System.out.println("Primos encontrados hasta ahora: " + parciales);
        System.out.println("Presiona ENTER para continuar");

        // Esperar ENTER
        try {
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } catch (Exception ignored) {}

        // REANUDAR
        synchronized (PAUSE_LOCK) {
            paused = false;
            PAUSE_LOCK.notifyAll();
        }

        // Finalizar
        for (Thread t : workers) {
            try {
                t.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }

        // Unir resultados
        List<Integer> all = new ArrayList<>();
        for (PrimeFinderThread t : workers) all.addAll(t.getPrimes());
        System.out.println("Total final de primos: " + all.size());
        return all;
    }
}

