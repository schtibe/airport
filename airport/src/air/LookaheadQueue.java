package air;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import p2pmpi.mpi.MPI;

public class LookaheadQueue {
	/**
	 * Use a fixed array to be faster
	 */
	List<Long> queues[];
	
	/**
	 * The amount of processes (and therefore incoming queues)
	 */
	int LPcount = 0;
	
	public LookaheadQueue() {
		this.LPcount = MPI.COMM_WORLD.SizeTotal();
		
		this.queues = new ArrayList[this.LPcount];
		for (int i = 0; i < this.LPcount; i++) {
			this.queues[i] = new ArrayList<Long>();
		}
	}
	
	/**
	 * Add lookahead to the corresponding queue
	 * 
	 * @param sender Who sent the lookahead 
	 * @param timestamp The lookahead value
	 */
	public synchronized void addLookahead(int sender, Long timestamp) {
		System.err.println("Adding a lookahead from " + sender + " with TS " + timestamp);
		this.queues[sender].add(timestamp);
		Collections.sort(this.queues[sender]);
	}
	
	/**
	 * Return the smallest lookahead
	 * 
	 * If one of the queues is empty, return null instead
	 * so the simulation knows not to proceed, because
	 * all queues have to have some value, otherwise it's not
	 * safe to proceed.
	 * The queues are sorted, so we are sure when we take
	 * the 0-position value we take the smallest from the queue
	 * to check which one's the smallest
	 */
	public synchronized Long getSmallestLookahead() {
		for (int i = 0; i < this.LPcount; i++) {
			if (this.queues[i].size() == 0) {
				return null;
			} 
		}
		
		// we are now sure we can do this
		Long smallestLookahead = this.queues[0].get(0);
		for (int i = 1; i < this.LPcount; i++) {
			smallestLookahead = Math.min(this.queues[i].get(0), smallestLookahead);
		}
		
		return smallestLookahead;
	}
	
	/**
	 * Remove the lookahead
	 * 
	 * Remove the lookahead from all the queues (in case
	 * the lookahead is present in several queues)
	 */
	public synchronized void removeLookeahead(Long lookahead) {
		for (int i = 0; i < this.LPcount; i++) {
			this.queues[i].remove(lookahead);
		}
	}
}
