package arsw.threads;

public class PausaMonitor {

    private boolean pausado = false;

    public synchronized void pausar() {
        pausado = true;
    }

    public synchronized void continuar() {
        pausado = false;
        notifyAll();
    }

    public synchronized void awaitSiPausado() throws InterruptedException {
        while (pausado) {
            wait();
        }
    }
}
