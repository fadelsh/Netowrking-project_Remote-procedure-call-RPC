import java.util.Arrays;
import java.lang.Byte;
import java.lang.Number;
public class Test {

	final static String IP_ADDRESS="127.0.0.1";
	final static int LENGTH_OF_OS_RESPONSE=13;
	final static int TCP_PORT=4321;
	public static void main (String[]arg) {
	  
		
		GetLocalTime timeObj=new GetLocalTime(); //1.Instantiate an RPC GetLocalTime object

		timeObj.valid.setValue('f'); //2. Set inputs
		timeObj.execute(IP_ADDRESS, TCP_PORT); // 3. Execute

		//4. Get outputs
		int t=timeObj.time.getValue(); 
		char v=timeObj.valid.getValue();

		/*
		 if time is NOT valid (e.g., sending a wrong command maybe 'getlocalSpeed' or something. 
		 Then it will just be 0. Basically anything other than "GetLocalTime" or "GetLocalOS" case senstive is considered invalid.
		 ivalidity can be tested by changing the cmd string in line 54 in GetLocalTime.java to a wrong command.
		 */
		System.out.println("Time is(HHMMSS): "+t); //If running the time sometime between 1am and 9:59 am then the format is HMMSS since integers can't hold a leading zero because they're meaningless
		System.out.println ("Validity of time is(y/n): "+ v); 
		
		//testing getlocal OS (same 4 steps as local time)
		GetLocalOS osObj=new GetLocalOS();
		osObj.execute(IP_ADDRESS, TCP_PORT);


		System.out.print("OS is: ");
		//loop through our char_c array to get the local OS (strings in C are char array!)
		for(int i=0; i< LENGTH_OF_OS_RESPONSE; i++){
			System.out.print(osObj.OS[i].getValue());
		}
		System.out.println();
		//if OS is valid then it will be "y", otherwise "n" for BOTH OS & valid instance variables (OS will just be "nnnnnnnnnnnnn" which means invalid).
		System.out.println("Validity of OS is(y/n): "+ osObj.valid.getValue());
		System.out.println();
		
}}

