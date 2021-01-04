package Commands.ConnectionCommands;

import com.fazecast.jSerialComm.SerialPort;

import java.io.IOException;

class SerialPortCommands extends ConnectionController {

    private SerialPort serialPort;

    public SerialPort getSerialPort() {
        return serialPort;
    }

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
