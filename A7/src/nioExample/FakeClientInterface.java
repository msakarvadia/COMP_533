package nioExample;

import java.nio.ByteBuffer;
import java.util.concurrent.ArrayBlockingQueue;

public interface FakeClientInterface {

	void processInput();

	ArrayBlockingQueue<ByteBuffer> getBoundedBuffer();

}
