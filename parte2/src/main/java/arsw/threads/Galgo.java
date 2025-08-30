package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 */
public class Galgo extends Thread {
    private int paso;
    private final Carril carril;
    private final RegistroLlegada regl;
    private final PausaMonitor pausa;

    public Galgo(Carril carril, String name, RegistroLlegada reg, PausaMonitor pausa) {
        super(name);
        this.carril = carril;
        this.paso = 0;
        this.regl = reg;
        this.pausa = pausa;
    }

    public void corra() throws InterruptedException {
        while (paso < carril.size()) {
            pausa.awaitSiPausado();
            Thread.sleep(100);
            carril.setPasoOn(paso++);
            carril.displayPasos(paso);

            if (paso == carril.size()) {
                carril.finish();
                int ubicacion = regl.asignarPuesto(this.getName()); // región crítica protegida
                System.out.println("El galgo " + this.getName() + " llego en la posicion " + ubicacion);
            }
        }
    }

    @Override
    public void run() {
        try {
            corra();
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }
}
