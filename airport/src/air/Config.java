package air;

import com.trolltech.qt.gui.QGraphicsScene;

public class Config {
	private static Config instance;

	private Config() {}
	
	public static Config getInstance() {
		if (Config.instance == null) {
			Config.instance = new Config();
		}
		
		return Config.instance;
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
		return 600;
	}
	
	public double getWorldWidth() {
		return 800;
	}
}
