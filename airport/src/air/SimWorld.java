package air;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

import p2pmpi.mpi.MPI;

import mpi.MpiMessage;
import mpi.RecvThread;
import mpi.SendThread;

import com.trolltech.qt.gui.QGraphicsScene;


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
	
	private SimWorld(){
		this.sendThread.start();
		this.recvThread.start();
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
	
	long timeScale = 100;

	public long getTimeScale() {
		return timeScale;
	}

	public void setTimeScale(long timeScale) {
		this.timeScale = timeScale;
	}
	
	String[] args;
	public void setArgs(String[] args) {
		this.args = args;
	}
	
	public String[] getArgs() {
		return this.args;
	}
	
	QGraphicsScene scene;
	public synchronized void setScene(QGraphicsScene scene) {
		this.scene = scene;
	}
	
	public synchronized QGraphicsScene getScene() {
		return this.scene;
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
	
	public int getSendRank() {
		int thisRank = MPI.COMM_WORLD.Rank();
		int procCount = MPI.COMM_WORLD.Size();
		
		return (thisRank + 1) % procCount;
	}
	
	public int getRecvRank() {
		int thisRank = MPI.COMM_WORLD.Rank();
		int procCount = MPI.COMM_WORLD.Size();
		
		return (thisRank + procCount - 1) % procCount;
	}
	
	public Queue<MpiMessage> incomingMessages = new LinkedList<MpiMessage>();
	
	public Queue<MpiMessage> outgoingMessages = new LinkedList<MpiMessage>();
	
	RecvThread recvThread = new RecvThread();
	
	SendThread sendThread = new SendThread();
}
