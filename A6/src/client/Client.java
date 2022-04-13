package client;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({ DistributedTags.CLIENT, DistributedTags.RMI, DistributedTags.GIPC, DistributedTags.NIO })
public class Client {
	public static void main(final String[] args) {
		try {

			// create client object
			//final ClientRemoteInterfaceRMI client = new ClientRemoteObject();
			final ClientRemoteInterfaceRMI client = new ClientRemoteObjectNIO();
			
			client.start(args);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
