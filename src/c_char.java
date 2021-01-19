/*
@fadelsh
the representation of a C char in java 
*/
public class c_char {
	 byte buf;
	
	public int getSize() {
		return 1; // size of char in C is 1
	}
	public char getValue() {
		char c = (char) (buf & 0xFF);
		return c;
		
	}
	public void setValue(byte b) {
		this.buf=b;
	}
	
	public void setValue(char v) {
		byte temp=(byte)v;
		this.buf=temp;
	}
	
	public byte toByte() {
		return buf;
	}


	
	
	
}
