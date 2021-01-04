package Commands.ConnectionCommands;

import java.io.IOException;

abstract class ConnectionController {

    public long getLIMIT_TIME() {
        return 10000;
    }

    public abstract void sendCommand(byte[] commandArray) throws IOException, InterruptedException;

    public abstract boolean checkInputStream(int minBytes) throws IOException, InterruptedException;

    public abstract byte[] readInputStream() throws IOException, InterruptedException;

    public abstract int getInputStream() throws IOException;

    public abstract boolean receiveEnq() throws IOException, InterruptedException;

}
