package au.herni.callcenter.service;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import au.herni.callcenter.exceptions.NoAvailableResponderException;
import au.herni.callcenter.facade.CallCenter;
import au.herni.callcenter.model.Director;
import au.herni.callcenter.model.Empleado;
import au.herni.callcenter.model.EstadoEmpleado;
import au.herni.callcenter.model.Operador;
import au.herni.callcenter.model.Supervisor;

/**
 * Implementación del servicio de atención de llamadas; también es un objeto ejecutable que será ejecutado por hilos
 * @author Hernán Camilo
 *
 */
public class DispatcherServiceImpl implements Dispatcher, Runnable {

	/**
	 * Base de números aleatorios para simular el tiempo de la llamada
	 */
	public static final Random ran = new Random(10000);
	/**
	 * Límites para el número aleatorio que representa los segundos que demora una llamada
	 */
	public static final int low = 5, high = 10;
	
	/**
	 * Cola de llamadas usada en caso de que existan más de 10 llamadas al tiempo
	 */
	private ConcurrentLinkedQueue<String> callQueue = new ConcurrentLinkedQueue<>();
	
	/**
	 * Método que atiende una llamada. Es invocado por el método run de la interface funcional.
	 * En caso de no existir un empleado disponible para atenderla, una excepción es elevada al método
	 * run de tal manera que un mensaje de no disponibilidad es mostrado
	 */
	public void dispatchCall() throws NoAvailableResponderException, InterruptedException {
		//System.out.println("número total de empleados: " + empleadosParaLlamadas.size());
		//System.out.println("empleados: " + empleadosParaLlamadas);
		int result = ran.nextInt(high-low) + low;
		List<Empleado> freeEmployees = CallCenter.getCallResponders().stream().filter(empleado -> empleado instanceof Operador)
									   .filter(e -> e.getEstadoEmpleado().equals(EstadoEmpleado.disponible))
									   .collect(Collectors.toList());
		System.out.println("número operadores libres: " + freeEmployees.size());
		if(!freeEmployees.isEmpty()) {
			Empleado callResponder = freeEmployees.get(0);
			if(!callQueue.isEmpty())
				attendCallInQueue(callResponder,callQueue, result);
			else
				attendCall(callResponder, result);
		}
		else {
			freeEmployees = CallCenter.getCallResponders().stream().filter(empleado -> empleado instanceof Supervisor)
							.filter(e -> e.getEstadoEmpleado().equals(EstadoEmpleado.disponible)).collect(Collectors.toList());
			System.out.println("número supervisores libres: " + freeEmployees.size());
			if(!freeEmployees.isEmpty()) {
				Empleado callResponder = freeEmployees.get(0);
				if(!callQueue.isEmpty())
					attendCallInQueue(callResponder,callQueue, result);
				else
					attendCall(callResponder, result);
			}
			else {
				freeEmployees = CallCenter.getCallResponders().stream().filter(empleado -> empleado instanceof Director)
						  	 	.filter(e -> e.getEstadoEmpleado().equals(EstadoEmpleado.disponible)).collect(Collectors.toList());
				System.out.println("número directores libres: " + freeEmployees.size());
				if(!freeEmployees.isEmpty()) {
					Empleado callResponder = freeEmployees.get(0);
					if(!callQueue.isEmpty())
						attendCallInQueue(callResponder,callQueue, result);
					else
						attendCall(callResponder, result);
				}
				else
				{
					callQueue.add("queued call"); // Se encola la llamada genéricamente
					throw new NoAvailableResponderException();
				}
			}
		}
	}
	
	/**
	 * Atiende una llamada encolada, simulando el tiempo de la llamada, ocupando al empleado, liberándolo después del tiempo y 
	 * elminando la llamada de la cabeza de la cola
	 * @param callResponder
	 * @param callQueue
	 * @param result
	 * @throws InterruptedException
	 */
	private void attendCallInQueue(Empleado callResponder, ConcurrentLinkedQueue<String> callQueue, int result) throws InterruptedException {
		callResponder.attendCall();
		TimeUnit.SECONDS.sleep(result);
		callResponder.free();
		callQueue.poll();
	}

	/**
	 * Atiende una llamada simulando el tiempo de la llamada, ocupando al empleado y liberándolo después del tiempo
	 * @param callResponder
	 * @param result
	 * @throws InterruptedException
	 */
	private void attendCall(Empleado callResponder, int result) throws InterruptedException {
		callResponder.attendCall();
		TimeUnit.SECONDS.sleep(result);
		callResponder.free();
	}

	/**
	 * 
	 */
	@Override
	public void run() {
		try {
				dispatchCall();
			} catch (NoAvailableResponderException e) {
				System.out.println("No hay personal para atender la llamada, intente en 15 segundos");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
	}	
}