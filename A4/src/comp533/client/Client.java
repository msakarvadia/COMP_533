package comp533.client;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({ DistributedTags.CLIENT, DistributedTags.RMI })
public class Client {
	public static void main(final String[] args) {
		try {

			// create client object
			final ClientRemoteObject client = new ClientRemoteObject();

			client.start(args);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
