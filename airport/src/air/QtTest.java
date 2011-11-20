package air;

import com.trolltech.qt.gui.QApplication;
import com.trolltech.qt.gui.QGraphicsRectItem;
import com.trolltech.qt.gui.QGraphicsScene;
import com.trolltech.qt.gui.QGraphicsView;

public class QtTest {
	  public static void main(String args[]) {
	        QApplication.initialize(args);
	 
	        QGraphicsScene scene = new QGraphicsScene();
	        QGraphicsRectItem rect = scene.addRect(0,0,100,100);
	        QGraphicsView view = new QGraphicsView(scene);
	        view.show();
	        
	 
	        QApplication.exec();
	    }
}
