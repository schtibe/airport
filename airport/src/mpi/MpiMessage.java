package mpi;

public class MpiMessage {

	private long timeStamp;
	private int type;
	
	private String aircraft = "";
	private String airport = "";
	
	public MpiMessage(long timeStamp, int type, String aircraft, String airport){
		setTimeStamp(timeStamp);
		setType(type);
		setAircraft(aircraft);
		setAirport(airport);
	}

	public long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getAircraft() {
		return aircraft;
	}

	public void setAircraft(String aircraft) {
		this.aircraft = aircraft;
	}

	public String getAirport() {
		return airport;
	}

	public void setAirport(String airport) {
		this.airport = airport;
	}
	
}
