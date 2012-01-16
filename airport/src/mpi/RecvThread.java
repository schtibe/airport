package mpi;

import air.SimWorld;
import p2pmpi.mpi.MPI;

public class RecvThread extends Thread {
	public void run() {
		int src = SimWorld.getInstance().getRecvRank();
		
		while(true) {
			MpiMessage[] data = { null };
			MPI.COMM_WORLD.Recv(data, 0, 1, MPI.OBJECT, MPI.ANY_SOURCE, 1);
			System.err.println("Received something");
		}
	}
}
