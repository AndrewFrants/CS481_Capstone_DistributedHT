package adzhiyev;
import java.io.IOException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;


public class RemoteDeviceDiscovery {

	public static void main(String args[]) throws IOException {
	    DiscoveryAgent discovery_agent = LocalDevice.getLocalDevice().getDiscoveryAgent();
	    
		RemoteDevice[] remote_device = discovery_agent.retrieveDevices(DiscoveryAgent.PREKNOWN);
		
		if(remote_device == null) {
			System.out.println("No Devices found");
			return;
		}
		for (RemoteDevice rd : remote_device) {
			System.out.println("The Devices name is : " + rd.getFriendlyName(false));
			System.out.println("Bluetooth Address : " + rd.getBluetoothAddress() + '\n');

		}

	}

}
