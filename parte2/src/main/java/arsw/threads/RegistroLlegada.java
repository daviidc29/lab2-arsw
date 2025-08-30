package arsw.threads;

public class RegistroLlegada {

	private int ultimaPosicionAlcanzada=1;

	private String ganador=null;
	
	public synchronized int asignarPuesto(String nombreGalgo) {
        int puesto = ultimaPosicionAlcanzada++;
        if (puesto == 1) {
            ganador = nombreGalgo;
        }
        return puesto;
    }

	public synchronized String getGanador() {
		return ganador;
	}

	public synchronized int getUltimaPosicionAlcanzada() {
		return ultimaPosicionAlcanzada;
	}	
	
}
