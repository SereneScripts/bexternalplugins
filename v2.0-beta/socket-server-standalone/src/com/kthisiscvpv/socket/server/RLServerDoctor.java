package com.kthisiscvpv.socket.server;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.kthisiscvpv.socket.client.RLClient;
import com.kthisiscvpv.util.logger.AbstractLogger;
import com.kthisiscvpv.util.logger.MockLogger.Level;

/**
 * Maintains the integrity of the client list.
 */
public class RLServerDoctor implements Runnable {

	// A RLClient is pronounced dead if it hasn't responded within DEADLOCK_TIME milliseconds.
	public static final long DEADLOCK_TIME = 60000L;

	private RLServer server;
	private AbstractLogger logger;

	public RLServerDoctor(RLServer server) {
		this.server = server;
		this.logger = this.server.getLogger();
	}

	@Override
	public void run() {
		while (true) {
			try {
				HashSet<RLClient> clients = this.server.getConnectedClients();
				List<RLClient> clientsClone;
				synchronized (clients) {
					clientsClone = new ArrayList<RLClient>(clients);
				}

				for (RLClient client : clientsClone) {
					long elapsedTime = System.currentTimeMillis() - client.getLastHeartbeat();
					if (elapsedTime >= DEADLOCK_TIME) {
						this.logger.println(RLServerDoctor.class, Level.INFO, client.getAddress() + " has stopped responding after " + elapsedTime + "ms and will be terminated");
						client.terminate();
					}
				}

				Thread.sleep(1000L);
			} catch (Exception e) {
				this.logger.println(RLServerDoctor.class, Level.FATAL, "Unable to start the maintain the integrity checks");
				e.printStackTrace();
				System.exit(1);
			}
		}
	}
}
