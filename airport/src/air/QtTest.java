package air;


import com.trolltech.qt.gui.*;

public class QtTest {
	  public static void main(String args[]) {
	        QApplication.initialize(args);
	 
	        QPushButton hello = new QPushButton("Hello World!");
	        hello.show();
	 
	        QApplication.exec();
	    }
}
