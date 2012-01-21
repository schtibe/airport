package air;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import mpi.MpiMessage;
import mpi.RecvThread;
import mpi.SendThread;


/**
 * SimWorld is a container class with airports and aircrafts whith unique name 
 * The class allows to access the simulation objects by name 
 * @author ps
 *
 */
public class SimWorld {
	private HashMap<String,Airport> airports = new HashMap<String,Airport>();
	private HashMap<String,Aircraft> aircrafts = new HashMap<String,Aircraft>();
	static private SimWorld instance = new SimWorld(); 
	
	private Simulator simulator;
	
	private LookaheadQueue lookaheadQueue;
	
	public LookaheadQueue getLookaheadQueue() {
		return lookaheadQueue;
	}

	public void setLookaheadQueue(LookaheadQueue lookaheadQueue) {
		this.lookaheadQueue = lookaheadQueue;
	}
	
	public void addAirport(Airport ap){
		if (airports.containsKey(ap.getName())) throw new RuntimeException("Duplicate airport name");
		airports.put(ap.getName(), ap);
	}
	public void addAircraft(Aircraft ac){
		if (aircrafts.containsKey(ac.getName())) throw new RuntimeException("Duplicate aircraft name");
		aircrafts.put(ac.getName(),ac);
	}
	public Airport getAirport(String name){
		return airports.get(name);
	}
	
	public Aircraft getAircraft(String name){
		return aircrafts.get(name);
	}
	
	public void removeAircraft(Aircraft ac) {
		aircrafts.remove(ac.getName());
	}
	
	public static SimWorld getInstance(){	
		return instance;
	}
	
	public HashMap<String, Airport> getAirports() {
		return this.airports;
	}
	
	public HashMap<String, Aircraft> getAircrafts() {
		return this.aircrafts;
	}
	
	public void setSimulator(Simulator s) {
		this.simulator = s;
	}
	
	public Simulator getSimulator() {
		return this.simulator;
	}

	@Override
	public String toString() {
		return "SimWorld [airports=" + airports + ", aircrafts=" + aircrafts
				+ "]";
	}
	
//	long timeScale = 100;
//
	/**
	 * @deprecated
	 */
	public long getTimeScale() {
		return getSimulator().getClock().getScaleFactor();
	}
//
//	public void setTimeScale(long timeScale) {
//		this.timeScale = timeScale;
//	}
	
	String[] args;
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public String[] getArgs() {
		return this.args;
	}

	public double getWorldScale() {
		return 250;
	}
	
	public double getXOffset() {
		return 2000;
	}
	
	public double getYOffset() {
		return 450;
	}
	
	public double getWorldHeight() {
		return 800;
	}
	
	public double getWorldWidth() {
		return 1000;
	}
	
	public int getRankFromString(String airport) {
		return this.airports.get(airport).getRank();
	}
	
	public Queue<MpiMessage> outgoingMessages = new LinkedList<MpiMessage>();
	
	private RecvThread recvThread;
	
	public RecvThread getRecvThread() {
		return recvThread;
	}

	public void setRecvThread(RecvThread recvThread) {
		this.recvThread = recvThread;
	}

	public SendThread getSendThread() {
		return sendThread;
	}

	public void setSendThread(SendThread sendThread) {
		this.sendThread = sendThread;
	}

	private SendThread sendThread;
}
