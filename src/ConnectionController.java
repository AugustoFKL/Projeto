import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.net.Socket;

public abstract class ConnectionController {
    private final long LIMIT_TIME = 10000;
    public long getLIMIT_TIME(){return LIMIT_TIME;}
    ConnectionController(){

    }
    public abstract void sendCommand(byte[] commandArray) throws IOException, InterruptedException;
    public abstract boolean checkInputStream(int minBytes) throws IOException, InterruptedException;
    public abstract byte[] readInputStream() throws IOException, InterruptedException;
    public abstract  int getInputStream() throws IOException;
    public abstract boolean receiveEnqs() throws IOException, InterruptedException;

}

class SocketCommands extends ConnectionController{

    private final Socket socket;

    SocketCommands(Socket socket) {
        super();
        this.socket = socket;
    }

    public Socket getSocket() { return socket; }

    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        boolean test = true;
        getSocket().getOutputStream().write(commandArray);
        Thread.sleep(10);
        while(System.currentTimeMillis()-start<getLIMIT_TIME() && test){
            if(checkInputStream(257)) {
                byte[] answer = readInputStream();
                test = answer[0] == 0x52;
                getSocket().getOutputStream().write(0x06);
                start = System.currentTimeMillis();
            }
        }
    }

    public boolean checkInputStream(final int minBytes) throws IOException {
        return (getSocket().getInputStream().available() > minBytes);
    }

    public byte[] readInputStream() throws IOException {
        byte[] inputRead = new byte[getSocket().getInputStream().available()];
        getSocket().getInputStream().read(inputRead);
        return inputRead;
    }

    @Override
    public int getInputStream() throws IOException {
        return getSocket().getInputStream().available();
    }

    @Override
    public boolean receiveEnqs() {
        return false;
    }
}

class SerialPortCommands extends ConnectionController {

    private final SerialPort serialPort;

    public SerialPort getSerialPort() {
        return serialPort;
    }

    SerialPortCommands(SerialPort serialPort) {
        super();
        this.serialPort = serialPort;
    }

    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        getSerialPort().getOutputStream().write(commandArray);
        Thread.sleep(10);
    }

    public boolean checkInputStream(final int minBytes) throws IOException {
        return (getSerialPort().getInputStream().available() > minBytes);
    }

    public byte[] readInputStream() throws IOException, InterruptedException {
        byte[] answer = new byte[getSerialPort().getInputStream().available()];
        getSerialPort().getInputStream().read(answer);
        return answer;
    }

    @Override
    public int getInputStream() throws IOException {
        return getSerialPort().getInputStream().available();
    }

    @Override
    public boolean receiveEnqs() throws IOException, InterruptedException {
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

class ConnectionCommands extends ConnectionController {
    private final String connectionType;
    private final SocketCommands socketCommands;
    private final SerialPortCommands serialPortCommands;
    private final String SOCKET_CONNECTION = "socket";
    private final String SERIAL_PORT_CONNECTION = "serialport";
    private final String[] args;
    private boolean enqsReceived = false;

    public String getSOCKET_CONNECTION() {
        return SOCKET_CONNECTION;
    }

    public String getSERIAL_PORT_CONNECTION() {
        return SERIAL_PORT_CONNECTION;
    }

    public String getConnectionType() {
        return connectionType;
    }

    ConnectionCommands(String connectionType, SerialPort serialPort, Socket socket, String[] args) {
        this.connectionType = connectionType;
        this.args = args;
        socketCommands = new SocketCommands(socket);
        serialPortCommands = new SerialPortCommands(serialPort);
    }

    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        if (args[4].equals("true") && !enqsReceived) {
            enqsReceived = receiveEnqs();
        }
        if (getConnectionType().equals(getSOCKET_CONNECTION())) {
            socketCommands.sendCommand(commandArray);
        } else{
            serialPortCommands.sendCommand(commandArray);
        }
    }

    public boolean checkInputStream(int minBytes) throws IOException, InterruptedException {
        boolean inputStream = false;
        if (getConnectionType().equals(getSOCKET_CONNECTION())) {
            inputStream = socketCommands.checkInputStream(minBytes);
        } else{
            inputStream = serialPortCommands.checkInputStream(minBytes);
        }
        return inputStream;
    }

    public byte[] readInputStream() throws IOException, InterruptedException {
        byte[] b;
        if (getConnectionType().equals(getSOCKET_CONNECTION())) {
            b = socketCommands.readInputStream();
        } else{
            b = serialPortCommands.readInputStream();
        }
        return b;
    }

    @Override
    public int getInputStream() throws IOException {
        int inputBytes = 0;
        if (getConnectionType().equals(getSOCKET_CONNECTION())) {
            inputBytes = socketCommands.getInputStream();
        } else {
            inputBytes = serialPortCommands.getInputStream();
        }
        return inputBytes;
    }

    @Override
    public boolean receiveEnqs() throws IOException, InterruptedException {
        if (getConnectionType().equals(getSOCKET_CONNECTION())) {
            return socketCommands.receiveEnqs();
        } else {
            return serialPortCommands.receiveEnqs();
        }
    }
}
