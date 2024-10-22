package forms;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import forms.App.RectangleBounds;


public class Client {
    public static void main(String[] args) {
        try {
            RemoteShapeManager shapeManager = (RemoteShapeManager) Naming.lookup("rmi://localhost/ShapeManager");
            List<RectangleBounds> rectangles = shapeManager.loadDrawing();
            // Do something with the loaded rectangles
            System.out.println("Loaded rectangles: " + rectangles);
        } catch (NotBoundException | MalformedURLException | RemoteException ex) {
            System.err.println("Failed to look up remote shape manager: " + ex.getMessage());
        }
    }
}