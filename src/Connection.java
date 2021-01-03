import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.net.Socket;

abstract class ConnectionController {

    public long getLIMIT_TIME(){ return 10000;}
    public abstract void sendCommand(byte[] commandArray) throws IOException, InterruptedException;
    public abstract boolean checkInputStream(int minBytes) throws IOException, InterruptedException;
    public abstract byte[] readInputStream() throws IOException, InterruptedException;
    public abstract  int getInputStream() throws IOException;
    public abstract boolean receiveEnq() throws IOException, InterruptedException;

}

class SocketCommands extends ConnectionController{

    private Socket socket;

    public boolean openSocket() throws IOException, InterruptedException {
        this.socket = new Socket("172.30.254.110",28016);
        return getSocket().isConnected();
    }

    public Socket getSocket() { return socket; }


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
    public boolean receiveEnq() throws IOException{
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

class SerialPortCommands extends ConnectionController {

    private SerialPort serialPort;

    public SerialPort getSerialPort() { return serialPort; }

    public boolean openSerialPort() {
        this.serialPort = SerialPort.getCommPorts()[0];
        getSerialPort().openPort();
        getSerialPort().setBaudRate(9600);
        getSerialPort().setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 20, 20);
        return getSerialPort().isOpen();
    }

    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        getSerialPort().getOutputStream().write(commandArray);
        Thread.sleep(10);
    }

    public boolean checkInputStream(final int minBytes) throws IOException {
        return (getSerialPort().getInputStream().available() > minBytes);
    }

    public byte[] readInputStream() throws IOException {
        byte[] answer = new byte[getSerialPort().getInputStream().available()];
        getSerialPort().getInputStream().read(answer);
        return answer;
    }

    @Override
    public int getInputStream() throws IOException {
        return getSerialPort().getInputStream().available();
    }

    @Override
    public boolean receiveEnq() throws IOException{
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
    private final String[] args;

    private final String SOCKET_TEXT = "socket";
    private final String SERIAL_PORT_TEXT = "serialport";

    private final SocketCommands socketCommands = new SocketCommands();
    private final SerialPortCommands serialPortCommands = new SerialPortCommands();

    private boolean enqReceived = false;

    public String getConnectionType() { return connectionType; }

    ConnectionCommands(String connectionType, String[] args){
        super();
        this.connectionType = connectionType.toLowerCase();
        this.args = args;
    }

    public boolean selectConnection() throws IOException, InterruptedException {

        if(getConnectionType().equals(SOCKET_TEXT)){ return socketCommands.openSocket(); }
        else if(getConnectionType().equals(SERIAL_PORT_TEXT)){ return serialPortCommands.openSerialPort(); }
        else{ System.out.println("Invalid connection type"); }

        return false;
    }

    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        if (args[4].equals("true") && !enqReceived) { enqReceived = receiveEnq(); }

        if (getConnectionType().equals(SOCKET_TEXT)) { socketCommands.sendCommand(commandArray); }
        else{ serialPortCommands.sendCommand(commandArray); }
    }

    public boolean checkInputStream(int minBytes) throws IOException, InterruptedException {
        if (getConnectionType().equals(SOCKET_TEXT)) { return socketCommands.checkInputStream(minBytes); }
        else{ return serialPortCommands.checkInputStream(minBytes); }
    }

    public byte[] readInputStream() throws IOException, InterruptedException {
        if (getConnectionType().equals(SOCKET_TEXT)) { return socketCommands.readInputStream(); }
        else{ return serialPortCommands.readInputStream(); }
    }

    @Override
    public int getInputStream() throws IOException {
        if (getConnectionType().equals(SOCKET_TEXT)) { return socketCommands.getInputStream(); }
        else { return serialPortCommands.getInputStream(); }
    }

    @Override
    public boolean receiveEnq() throws IOException, InterruptedException {
        if (getConnectionType().equals(SOCKET_TEXT)) { return socketCommands.receiveEnq(); }
        else { return serialPortCommands.receiveEnq(); }
    }
}
