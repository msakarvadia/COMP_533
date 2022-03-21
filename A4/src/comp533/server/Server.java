package comp533.server;

import util.annotations.Tags;
import util.tags.DistributedTags;

@Tags({ DistributedTags.SERVER, DistributedTags.RMI })
public class Server {

	public static void main(final String[] args) {
		final ServerRemoteObject server = new ServerRemoteObject();

		server.start(args);
	}

}
