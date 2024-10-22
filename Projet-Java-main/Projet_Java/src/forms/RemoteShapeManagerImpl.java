package forms;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import forms.App.RectangleBounds;

public class RemoteShapeManagerImpl extends UnicastRemoteObject implements RemoteShapeManager {
    /**
	 * 
	 */
	private static final long serialVersionUID = 8305451104322455675L;

	public RemoteShapeManagerImpl() throws RemoteException {
        super();
    }

    @Override
    public void saveDrawing(List<RectangleBounds> rectangles) throws RemoteException {
        try {
            Saver.saveDrawing(rectangles, "remote_drawing.ser");
            System.out.println("Drawing saved remotely.");
        } catch (IOException ex) {
            System.err.println("Failed to save drawing: " + ex.getMessage());
        }
    }

    @Override
    public List<RectangleBounds> loadDrawing() throws RemoteException {
        try {
            List<RectangleBounds> rectangles = Loader.loadDrawing("remote_drawing.ser");
            System.out.println("Drawing loaded remotely.");
            return rectangles;
        } catch (IOException | ClassNotFoundException ex) {
            System.err.println("Failed to load drawing: " + ex.getMessage());
            return null;
        }
    }
}