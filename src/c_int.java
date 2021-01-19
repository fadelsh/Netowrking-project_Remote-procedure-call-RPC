/*
@fadelsh
 The representation of a c integer in java
*/
public class c_int {

	 byte[] buf = new byte[4];  //little endian 
	
	public int getSize() { // the size of buf
		return buf.length;	
	}
	
	public int getValue() { // the int value represented by buf
		return this.fromByteArray();
		
	}
	
	public void setValue(byte[] b) { // copy the value in b into buf
		this.buf=b;
	}
	
	public void setValue(int v) { //set buf according to v
		byte[]temp=toBytes(v);
		this.buf=temp;
		
	}
	public byte[] toByte() { //return buf
		return buf;
		
	}

//Below are helper functions to convert from and to bytes
	private int fromByteArray() {
        return ((buf[0] & 0xFF) << 24) | 
               ((buf[1] & 0xFF) << 16) | 
               ((buf[2] & 0xFF) << 8 ) | 
               ((buf[3] & 0xFF) << 0 );
   }
	
	 private byte[] toBytes(int i) {
        byte[] result = new byte[4];

        result[0] = (byte) (i >> 24);
        result[1] = (byte) (i >> 16);
        result[2] = (byte) (i >> 8);
        result[3] = (byte) (i /*>> 0*/);
       
        return result;
    }
}
