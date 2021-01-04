package Commands.ConnectionCommands;

import java.io.IOException;

public class ConnectionCommands extends ConnectionController {
    private final String connectionType;
    private final String[] args;

    private final String SOCKET_TEXT = "socket";
    private final String SERIAL_PORT_TEXT = "serialport";

    private final SocketCommands socketCommands = new SocketCommands();
    private final SerialPortCommands serialPortCommands = new SerialPortCommands();

    private boolean enqReceived = false;

    public ConnectionCommands(String connectionType, String[] args) {
        super();
        this.connectionType = connectionType.toLowerCase();
        this.args = args;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public boolean selectConnection() throws IOException, InterruptedException {

        if (getConnectionType().equals(SOCKET_TEXT)) {
            return socketCommands.openSocket();
        } else if (getConnectionType().equals(SERIAL_PORT_TEXT)) {
            return serialPortCommands.openSerialPort();
        } else {
            System.out.println("Invalid connection type");
        }

        return false;
    }

    public void sendCommand(byte[] commandArray) throws IOException, InterruptedException {
        if (args[4].equals("true") && !enqReceived) {
            enqReceived = receiveEnq();
        }

        if (getConnectionType().equals(SOCKET_TEXT)) {
            socketCommands.sendCommand(commandArray);
        } else {
            serialPortCommands.sendCommand(commandArray);
        }
    }

    public boolean checkInputStream(int minBytes) throws IOException, InterruptedException {
        if (getConnectionType().equals(SOCKET_TEXT)) {
            return socketCommands.checkInputStream(minBytes);
        } else {
            return serialPortCommands.checkInputStream(minBytes);
        }
    }

    public byte[] readInputStream() throws IOException, InterruptedException {
        if (getConnectionType().equals(SOCKET_TEXT)) {
            return socketCommands.readInputStream();
        } else {
            return serialPortCommands.readInputStream();
        }
    }

    @Override
    public int getInputStream() throws IOException {
        if (getConnectionType().equals(SOCKET_TEXT)) {
            return socketCommands.getInputStream();
        } else {
            return serialPortCommands.getInputStream();
        }
    }

    @Override
    public boolean receiveEnq() throws IOException, InterruptedException {
        if (getConnectionType().equals(SOCKET_TEXT)) {
            return socketCommands.receiveEnq();
        } else {
            return serialPortCommands.receiveEnq();
        }
    }
}
