package forms;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Server {
	public static void main(String[] args) {
	    try {
	        RemoteShapeManagerImpl remoteShapeManager = new RemoteShapeManagerImpl();
	        String host = "localhost"; // or specify the host where the RMI registry is running
	        int port = 1099; // or specify the port where the RMI registry is running
	        Registry registry = LocateRegistry.getRegistry(host, port);
	        registry.rebind("RemoteShapeManager", remoteShapeManager);
	        System.out.println("Server is ready.");
	    } catch (Exception e) {
	        System.err.println("Server exception: " + e.toString());
	        e.printStackTrace();
	    }
	}
}
