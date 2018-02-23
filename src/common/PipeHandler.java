package common;

import java.io.*;

public interface PipeHandler {
	public void processInputStream(int id, InputStream is) throws Exception;
	public void processOutputStream(int id, OutputStream os) throws Exception;
}
