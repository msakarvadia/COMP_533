package nioExample;

import assignments.util.mainArgs.ServerArgsProcessor;

public class NIOManagerPrintServerLauncherNoFactory {
	public static void main(String[] args) {
		new AnNIOManagerPrintServer(ServerArgsProcessor.getNIOServerPort(args));
	}

}