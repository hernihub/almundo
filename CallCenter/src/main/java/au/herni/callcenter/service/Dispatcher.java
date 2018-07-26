package au.herni.callcenter.service;


import au.herni.callcenter.exceptions.NoAvailableResponderException;
/**
 * Servicio de atención de llamadas
 * @author Hernán Camilo
 *
 */
public interface Dispatcher extends Runnable {	
	/**
	 * Asigna la llamada a los empleados disponibles
	 * @param callId 
	 * @param callId
	 * @throws InterruptedException 
	 */
	public void dispatchCall() throws NoAvailableResponderException, InterruptedException;
}
