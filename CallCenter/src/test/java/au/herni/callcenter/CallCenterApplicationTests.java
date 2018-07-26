package au.herni.callcenter;

import org.junit.Test;
import au.herni.callcenter.exceptions.NoAvailableResponderException;
import au.herni.callcenter.facade.CallCenter;


public class CallCenterApplicationTests {
	
	private int numberOfCalls = 20;
	
	private CallCenter callcenter;
	
	
	@Test
	public void contextLoads() {
		System.out.println("callcenter: " + callcenter);
	}
	
	/**
	 * 
	 * @throws InterruptedException
	 * @throws NoAvailableResponderException
	 */
	@Test
	public void testConcurrentCalls() throws InterruptedException, NoAvailableResponderException {
		callcenter.dispatchCalls(numberOfCalls);
	}
}
