package arsw.threads;

public class PausaMonitor {

    private boolean pausado = false;

    /** Pone el sistema en pausa (los galgos se bloquearán al llegar al await). */
    public synchronized void pausar() {
        pausado = true;
    }

    /** Quita la pausa y despierta a TODOS los hilos que estaban esperando. */
    public synchronized void continuar() {
        pausado = false;
        notifyAll();
    }

    /** Punto de espera cooperativo para los galgos. */
    public synchronized void awaitSiPausado() throws InterruptedException {
        while (pausado) {
            wait();
        }
    }
}
