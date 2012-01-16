package mpi;

import p2pmpi.mpi.MPI;
import air.SimWorld;

public class SendThread extends Thread {
	public void run() {
		int dest = SimWorld.getInstance().getSendRank();
		
		while(true) {
			MpiMessage data = SimWorld.getInstance().outgoingMessages.poll();
			if (data != null) {
				MpiMessage[] dataBuf = new MpiMessage[1];
				dataBuf[0] = data;
				MPI.COMM_WORLD.Send(dataBuf, 0, 1, MPI.OBJECT, dest, 1);
			}
		}
	}
}
