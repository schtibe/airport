package mpi;

import java.io.Serializable;

public class MpiMessage implements Serializable {

	/**
	 * 
	 */
	public static final long serialVersionUID = -4427009182037906959L;
	public long timeStamp;
	public int type;
	
	public String aircraft = "";
	public String airport = "";
	
	public String testText;
	
}
