package forms;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

import forms.App.RectangleBounds;

public interface RemoteShapeManager extends Remote {
    void saveDrawing(List<RectangleBounds> rectangles) throws RemoteException;
    List<RectangleBounds> loadDrawing() throws RemoteException;
}