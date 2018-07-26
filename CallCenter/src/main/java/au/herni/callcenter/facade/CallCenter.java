package au.herni.callcenter.facade;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import au.herni.callcenter.model.Empleado;
import au.herni.callcenter.model.Operador;
import au.herni.callcenter.service.Dispatcher;
import au.herni.callcenter.service.DispatcherServiceImpl;

/**
 * Esta clase representa el call center donde existe un despachador de llamadas.
 * @author Hernán Camilo
 *
 */
public class CallCenter {
	
	/**
	 * La lista de empleados disponibles
	 */
	private static List<Empleado> callRespondersThreadSafeList = null;
	
	/**
	 * El despachador inyectado por Spring
	 */
	private Dispatcher dispatcher = new DispatcherServiceImpl();
	
	/**
	 * Datos iniciales
	 */
	static {
		List<Empleado> unsafeList = new ArrayList<>();
		unsafeList.add(new Operador(1));
		unsafeList.add(new Operador(2));
		unsafeList.add(new Operador(3));
		unsafeList.add(new Operador(4));
		unsafeList.add(new Operador(5));
		unsafeList.add(new Operador(6));
		unsafeList.add(new Operador(7));
		unsafeList.add(new Operador(8));
		unsafeList.add(new Operador(9));
//		unsafeList.add(new Supervisor(10));
//		unsafeList.add(new Supervisor(11));
//		unsafeList.add(new Director(12));
		callRespondersThreadSafeList = Collections.synchronizedList(unsafeList);
	}
	
	public CallCenter() {
		System.out.println("CallCenter app initiated");
	}
	
	/**
	 * El método que atiende un número de llamadas mediante un servicio de ejecución de hilos
	 * @param numCalls
	 */
	public void dispatchCalls(int numCalls) {
		ExecutorService executor = Executors.newFixedThreadPool(numCalls);
		
		for (int i = 0; i < numCalls; i++)
			executor.execute(dispatcher);

		executor.shutdown();
		// esperar a todos los hilos
		while (!executor.isTerminated()) {
		}
		System.out.println("\nTodos los hilos (llamadas) terminadas");
	}
	
	/**
	 * Acceso a la lista de empleados disponibles para atender la llamada
	 * @return
	 */
	public static List<Empleado> getCallResponders() {
		return callRespondersThreadSafeList;
	}
}