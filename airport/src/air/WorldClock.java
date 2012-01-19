package air;

public class WorldClock {
	private long currentSimulationTime = 0;

	public static final long REPAINT_GAP = 50;

	private static final int SCALE_FACTOR = 300;

	public long currentSimulationTime() {
		return this.currentSimulationTime;
	}
	
	public boolean isInPast(long time) {
		return time < this.currentSimulationTime;
	}

	public void sleepUntil(long targetTime) {
		final long diff = targetTime - this.currentSimulationTime;
		try {
			final long sleepTime = diff * 1000 / SCALE_FACTOR;
			Thread.sleep(sleepTime);
			this.currentSimulationTime = targetTime;
			
		} catch (InterruptedException e) {
		}
	}
}
