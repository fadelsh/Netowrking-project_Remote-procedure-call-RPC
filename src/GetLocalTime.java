/*
@fadelsh
My RPC implemention of LocalTime class
*/
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Arrays;
//import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Array;



public class GetLocalTime {
		
	c_int time;
	c_char valid;
	
	public GetLocalTime() {
		this.time=new c_int();
		this.valid=new c_char();
		time.setValue(0); //default time
		valid.setValue('f'); //default is false (invalid)
	}

	public StringBuilder convert(byte[] buf){
        if (buf == null){
            return null;
	}
	if(buf.length==0){
		System.out.println("Empty");
	}
        StringBuilder ret = new StringBuilder();
        int i = 0;
        while (i<7){
            ret.append((char) buf[i]);
            i++;
        }
        return ret;
    }

	
	public int execute(String IP, int port) {
		//1.Create a binary buffer
		int length=time.getSize()+valid.getSize();
		byte []buf=new byte[100+4+length];
		
		//2.Marshall parameters into the buffer
		String cmd="GetLocalTime";
		byte[] cmdID = cmd.getBytes(); //1 CmdID (100 bytes): the command ID
		int offset=100;

		byte[]cmdLength=toBytes(length); //2 CmdLength (4 bytes): the length of CmdBuf
		
		byte [] cmdBufTime=time.toByte(); //3 CmdBuf (dynamic): the parameters to the command
		
		byte charValid=valid.toByte();
		
		byte [] cmdBufValid= {charValid}; //4 CmdBuf (dynamic): the parameters to the command

		System.arraycopy(cmdID, 0, buf,0, cmdID.length);
		System.arraycopy(cmdLength, 0, buf, offset, 4);
		offset=offset+4;
		System.arraycopy(cmdBufTime, 0, buf, offset, time.getSize());
		offset=offset+time.getSize();
		System.arraycopy(cmdBufValid, 0, buf, offset, valid.getSize());
		offset=offset+valid.getSize();

		
		 try {

			//3. Send/receive the buffer to/from the RPC server
			Socket TCPSocket=new Socket(IP, port);
			System.out.println();
            System.out.println("A TCP connection  has been established in port "+port);
            DataInputStream inStream  = new DataInputStream(TCPSocket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(TCPSocket.getOutputStream());

			outStream.write(buf,0,buf.length);
			byte[]rec=new byte[109];

			inStream.readFully(rec);

			byte[]timeRec=new byte[7];
			int j=0; 
			for(int i=12; i<19;i++){
				timeRec[j]=rec[i];
				j++;
			}
		
			StringBuilder builder=convert(timeRec);
			String r=builder.toString();
			char validValue=r.charAt(r.length()-1);
			
			int timeVal=0;
			if(validValue=='y'){
				String seprateTimeFromValid= r.substring(0,6);
				timeVal=Integer.parseInt(seprateTimeFromValid);
			}
			
			//4.Set parameters according to the buffer
			time.setValue(timeVal);
			valid.setValue(validValue);

		} catch (IOException e) {
			System.out.println ("Exception in GetLocalTime class");
		}
		
return 0;
	}

	static int toInteger32L(byte[] bytes)
    {
    	int tmp = (bytes[0]&0xff) | 
    	          ((bytes[1]&0xff) << 8) | 
    	          ((bytes[2]&0xff) << 16) | 
    	          ((bytes[3]&0xff) << 24);
    	             
    	return tmp;
    }
	
	 public byte[] toBytes(int i) {
	        byte[] result = new byte[4];

	        result[0] = (byte) (i >> 24);
	        result[1] = (byte) (i >> 16);
	        result[2] = (byte) (i >> 8);
	        result[3] = (byte) (i /*>> 0*/);
	       
	        return result;
		}
		
		public int fromByteArray(byte[] buf) {
			return ((buf[0] & 0xFF) << 24) | 
				   ((buf[1] & 0xFF) << 16) | 
				   ((buf[2] & 0xFF) << 8 ) | 
				   ((buf[3] & 0xFF) << 0 );
	   }
	
}
