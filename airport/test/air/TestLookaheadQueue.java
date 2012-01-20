package air;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This class was only temporarily used to test the functionalities
 * of the lookahead queue. To make it work again, the logger would have
 * to be implemented in this test and the MPI-Routines would have to be romoved.
 */
public class TestLookaheadQueue {
	
	LookaheadQueue lookaheadQueue;
	
	@Before
	public void before() {
		this.lookaheadQueue = new LookaheadQueue();
	}
	
	@Test
	public void testAdd() {
		this.lookaheadQueue.addLookahead(0, 10l);
		this.lookaheadQueue.addLookahead(1, 20l);
		this.lookaheadQueue.addLookahead(2, 30l);
		
		assertEquals(new Long(10), this.lookaheadQueue.getSmallestLookahead());
	}
	
	@Test
	public void testAddWithManyValues() {
		this.lookaheadQueue.addLookahead(0, 10l);
		this.lookaheadQueue.addLookahead(1, 20l);
		this.lookaheadQueue.addLookahead(2, 78l);
		this.lookaheadQueue.addLookahead(0, 6l);
		this.lookaheadQueue.addLookahead(2, 70l);
		this.lookaheadQueue.addLookahead(1, 110l);
		this.lookaheadQueue.addLookahead(0, 10l);
		this.lookaheadQueue.addLookahead(2, 13l);
		this.lookaheadQueue.addLookahead(2, 55l);
		this.lookaheadQueue.addLookahead(1, 5l);
		this.lookaheadQueue.addLookahead(0, 15l);
		
		assertEquals(new Long(5), this.lookaheadQueue.getSmallestLookahead());
	}
	
	@Test
	public void testSimpleRemoval() {
		this.lookaheadQueue.addLookahead(0, 10l);
		this.lookaheadQueue.addLookahead(0, 10l);
		
		this.lookaheadQueue.removeLookeahead(10l);
		
		assertTrue(this.lookaheadQueue.hasEmptyQueues());
	}
	
	@Test
	public void testBigRemoval() {
		this.lookaheadQueue.addLookahead(0, 10l);
		this.lookaheadQueue.addLookahead(1, 20l);
		this.lookaheadQueue.addLookahead(2, 78l);
		this.lookaheadQueue.addLookahead(0, 6l);
		this.lookaheadQueue.addLookahead(2, 70l);
		this.lookaheadQueue.addLookahead(1, 110l);
		this.lookaheadQueue.addLookahead(0, 10l);
		this.lookaheadQueue.addLookahead(2, 13l);
		this.lookaheadQueue.addLookahead(2, 55l);
		this.lookaheadQueue.addLookahead(1, 5l);
		this.lookaheadQueue.addLookahead(0, 15l);
	
		this.lookaheadQueue.removeLookeahead(5l);
		assertEquals(new Long(6), this.lookaheadQueue.getSmallestLookahead());
		this.lookaheadQueue.removeLookeahead(6l);
		assertEquals(new Long(10), this.lookaheadQueue.getSmallestLookahead());
		this.lookaheadQueue.removeLookeahead(10l);
		assertEquals(new Long(13), this.lookaheadQueue.getSmallestLookahead());
	}
	
	@Test
	public void testEmptyness() {
		this.lookaheadQueue.addLookahead(0, 10l);
		assertTrue(this.lookaheadQueue.hasEmptyQueues());
		this.lookaheadQueue.addLookahead(1, 11l);
		assertTrue(this.lookaheadQueue.hasEmptyQueues());
		this.lookaheadQueue.addLookahead(2, 12l);
		assertFalse(this.lookaheadQueue.hasEmptyQueues());
	}
}
