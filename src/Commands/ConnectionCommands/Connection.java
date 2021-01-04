package Commands.ConnectionCommands;

import java.io.IOException;
import java.net.Socket;

class SocketCommands extends ConnectionController {

    private Socket socket;

    public boolean openSocket() throws IOException, InterruptedException {
        this.socket = new Socket("172.30.254.110", 28016);
        return getSocket().isConnected();
    }

    public Socket getSocket() {
        return socket;
    }


    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        getSocket().getOutputStream().write(commandArray);
        Thread.sleep(10);
    }

    public boolean checkInputStream(final int minBytes) throws IOException {
        return (getSocket().getInputStream().available() > minBytes);
    }

    public byte[] readInputStream() throws IOException {
        byte[] answer = new byte[getSocket().getInputStream().available()];
        getSocket().getInputStream().read(answer);
        return answer;
    }

    @Override
    public int getInputStream() throws IOException {
        return getSocket().getInputStream().available();
    }

    @Override
    public boolean receiveEnq() throws IOException {
        final long start = System.currentTimeMillis();
        int enqReceived = 0;
        while (System.currentTimeMillis() - start < getLIMIT_TIME() / 4) {
            byte[] input = readInputStream();
            for (byte b : input)
                if (b == 0x05) {
                    enqReceived++;
                }
            if (enqReceived >= 5) {
                return true;
            }
        }
        return false;
    }
}

