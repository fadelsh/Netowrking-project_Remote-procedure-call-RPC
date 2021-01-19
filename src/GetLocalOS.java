/*
@fadelsh
My RPC implemention of LocalOS class
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


public class GetLocalOS {
    c_char[] OS;
	c_char valid;

    public GetLocalOS() {
        this.OS=new c_char [16]; 
        this.valid=new c_char();
        //intialize it to anyting to avoid null pointer exceptions
        for(int i=0; i<16;i++){
            OS[i]=valid;
        }

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
        while (i<14){
            ret.append((char) buf[i]);
            i++;
        }
        return ret;
    }

    public int execute(String IP, int port) {

        //1. Create a binary buffer
        int length=OS.length+valid.getSize();
		byte []buf=new byte[100+4+length];
		
		String cmd="GetLocalOS";
		byte[] cmdID = cmd.getBytes(); //1
        int offset=100;
        

        // 2.Marshall parameters into the buffer

		byte[]cmdLength=toBytes(length); //2
        
        //3
        byte [] cmdBufOS=new byte[16];
        for(int i=0; i<OS.length;i++){
            byte charOS=OS[i].toByte();
            cmdBufOS[i]=charOS;
        }

		byte charValid=valid.toByte();
		
        byte [] cmdBufValid= {charValid}; //4

        
        System.arraycopy(cmdID, 0, buf,0, cmdID.length);
		System.arraycopy(cmdLength, 0, buf, offset, 4);
		offset=offset+4;
		System.arraycopy(cmdBufOS, 0, buf, offset, OS.length);
		offset=offset+OS.length;
		System.arraycopy(cmdBufValid, 0, buf, offset, valid.getSize());
        offset=offset+valid.getSize();

        try{
            // 3. Send/receive the buffer to/from the RPC server
            Socket TCPSocket=new Socket(IP, port);
            System.out.println();
            System.out.println("A TCP connection  has been established in port "+port);
            DataInputStream inStream  = new DataInputStream(TCPSocket.getInputStream());
            DataOutputStream outStream = new DataOutputStream(TCPSocket.getOutputStream());

			
			outStream.write(buf,0,buf.length);

			byte[]rec=new byte[109];

            inStream.readFully(rec);
            
            byte[]osRec=new byte[14];
            int j=0;
            for(int i=10; i<24;i++){
				osRec[j]=rec[i];
				j++;
            }
            
            StringBuilder builder=convert(osRec);
            String r=builder.toString();
            char validValue=r.charAt(r.length()-1);


            // 4.Set parameters according to the buffer
            if(r.charAt(8)=='n'){
                valid.setValue(r.charAt(8));
                return 0;
            }
        
         if(validValue=='y'){
               for(int i=0; i<13;i++){
                   c_char c=new c_char();
                   c.setValue(r.charAt(i));
                   OS[i]=c;
               }
         }
            valid.setValue(validValue);

    } catch (IOException e) {
        System.out.println ("Exception in GetLocalOS class");
    }
    
    return 0;
    }

    public byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);
       
        return result;
    }
}
