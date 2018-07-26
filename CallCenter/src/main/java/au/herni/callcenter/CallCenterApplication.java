package au.herni.callcenter;

import au.herni.callcenter.facade.CallCenter;

/**
 * Simulador de un call center
 * @author Hernán Camilo
 *
 */
public class CallCenterApplication {
	
	static int numCalls = 12;
	
	public static void main(String[] args) {
		new CallCenter().dispatchCalls(numCalls);
	}
}