import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;
import java.net.Socket;

public class ConnectionSelect {

    private final String typeofConnection;
    private final String[] args;
    private Socket socket;
    private SerialPort serialPort;

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public String[] getArgs() {
        return args;
    }

    public Socket getSocket() {
        return socket;
    }

    public ConnectionSelect(String typeofConnection, String[] args){
        this.typeofConnection = typeofConnection.toLowerCase();
        this.args = args;   
    }

    private String getTypeofConnection() {
        return typeofConnection;
    }

    //** Try to open the comport and start the CommandsComPort class **//
    private void openComPort() throws IOException, InterruptedException {
        this.serialPort = SerialPort.getCommPorts()[0];
        getSerialPort().openPort();
        getSerialPort().setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);

    }

    //** Try to open the socket and start the CommandsSocket class **//
    private void openSocket() throws IOException, InterruptedException {
        this.socket =new Socket("172.30.254.110",28016);
    }

    public void selectConnectionType() throws IOException, InterruptedException {
        switch (getTypeofConnection()) {
            case "socket":
                openSocket();
                break;
            case "serialport":
                openComPort();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + getTypeofConnection());
        }

        if(getSerialPort().isOpen() || getSocket().isConnected()) {

            final ConnectionCommands connectionCommands;
            connectionCommands = new ConnectionCommands(getTypeofConnection(), getSerialPort(), getSocket(), getArgs());
            final Protocol protocol = new Protocol(getArgs(), connectionCommands);
            protocol.commandsStart();
        }
    }
}
