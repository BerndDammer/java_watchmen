package watchmen.networkinterfacewatch;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Logger;

import wefwefwe.common;

public class NetworkInterfaceWatcher extends Thread {
	private final NetworkInterfaceParent parent;

	enum SCAN_STATE {
		INIT, NONE, SINGLE, MULTI, AFTER;
	}

	private SCAN_STATE scan_state = SCAN_STATE.INIT;

	private final Logger logger = Logger.getAnonymousLogger();

	private final Map<String, NetworkInterfaceHolder> last = new TreeMap<>();
	private final Map<String, NetworkInterfaceHolder> act = new TreeMap<>();

	public NetworkInterfaceWatcher(NetworkInterfaceParent parent) {
		this.parent = parent;
		setName("NetworkInterfaceWatcher");
		start();
	}

	@Override
	public void run() {
		boolean searching = true;
		last.clear();
		while (searching) {
			delay(3000);
			{
				act.clear();
				Enumeration<NetworkInterface> eni;
				try {
					eni = NetworkInterface.getNetworkInterfaces();
					while (eni.hasMoreElements()) {
						NetworkInterface ni = eni.nextElement();
						String name = ni.getName();
						if (last.containsKey(name)) {
							NetworkInterfaceHolder nih = last.get(name);
							act.put(name, nih);
							nih.check(ni);
						} else {
							NetworkInterfaceHolder nih = new NetworkInterfaceHolder(ni);
							act.put(name, nih);
							last.put(name, nih);
						}
					}
					for (String name : last.keySet()) {
						if (!act.containsKey(name)) {
							last.remove(name);
							logger.info("Interface " + name + " removed");
						}
					}
				} catch (SocketException e1) {
					logger.info("Socket Exception by getting NetworkInterfaces");
				}
			}
			// /////////////////////////////////////////
			// final result building
			//
			{
				int countUsables = 0;
				NetworkInterfaceHolder theOneAndOnly = null;
				for (NetworkInterfaceHolder nih : last.values()) {
					if (nih.isGood()) {
						countUsables++;
						theOneAndOnly = nih;
					}
				}
				if (countUsables == 1) {
					if (common.LOG_HABEMUS_PAPAM)
						logger.info("Habemus papam : " + theOneAndOnly.getName());
					parent.habemusPapam();
					searching = false;
				} else {
					logger.info("Overwrite Network Interface search with : " + "eth2");
					parent.habemusPapam();
					searching = false;
				}
			}
		}
	}

	private void delay(int delay) {
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			logger.warning("interrupted delay");
		}
	}
}
