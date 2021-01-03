import ABNTCommands.ParametersReadingSimpleAnswer;
import Util.ByteArrayUtils;
import Util.CRC16CAS;

import java.io.IOException;

class Protocol {
    
    private final String[] args;
    private final ConnectionCommands connectionCommands;
    private int writtenLines = 0;
    private byte[] commandArray = new byte[66];
    private final long LIMIT_TIME = 20000;

    private boolean endAnswer = false;

    private String[] getArgs() { return args; }

    public ConnectionCommands getConnectionCommands() { return connectionCommands; }

    public int getWrittenLines() { return writtenLines; }

    public void setWrittenLines(int writtenLines) { this.writtenLines = writtenLines; }

    public long getLIMIT_TIME() { return LIMIT_TIME; }

    public Protocol(String[] args, ConnectionCommands connectionCommands){
        this.args = args;
        this.connectionCommands = connectionCommands;
    }

    public void commandsStart() throws IOException, InterruptedException {
        final String command = getArgs()[1];
        switch (command) {
            case "TOT":
                setArray("0x51");
                sendStarter();
                Thread.sleep(10);
                setArray("0x80");
                sendStarter();
                Thread.sleep(10);
                setArray("0x52");
                endAnswer = true;
                sendStarter();
                break;
            case "TOD":
                setArray("0x21");
                endAnswer = true;
                sendStarter();
                break;
            case "0xC1":
                setArray("0xC1");
                endAnswer = true;
                sendStarter();
                break;
            case "0x51":
                setArray((byte) 0x51);
                endAnswer = true;
                sendStarter();
        }

    }

    private void setArray(String command){
        ParametersReadingSimpleAnswer parametersReadingSimpleAnswer = new ParametersReadingSimpleAnswer();
        commandArray = parametersReadingSimpleAnswer.setCommandArray(command,"123456");


        if(command.equals("0xC1")){
            commandArray[4] = (byte) 0x41;
            commandArray[7] = (byte) 0x01;
        }
        generateCRC();
    }

    private void generateCRC() {
        int crc = CRC16CAS.calculate(commandArray, 0, 64);
        byte msb = CRC16CAS.getMSB(crc);
        byte lsb = CRC16CAS.getLSB(crc);
        commandArray[64] = lsb;
        commandArray[65] = msb;
    }

    private void sendStarter() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        boolean isCompost = true;
        connectionCommands.sendCommand(commandArray);
        while(System.currentTimeMillis()-start < getLIMIT_TIME()/5 && isCompost){
            if(getConnectionCommands().checkInputStream(257)) {
                connectionCommands.sendCommand(new byte[] {0x06});
                byte[] answer = getConnectionCommands().readInputStream();
                isCompost = answer[0] == 0x52;
                callPrintParameters(answer);
                if(isCompost){
                    start = System.currentTimeMillis();
                }
            }
        }
    }

    private boolean checkCRC(final byte[] reading) {
        String crc = ByteArrayUtils.byteToHex(reading[reading.length - 1]) + ByteArrayUtils.byteToHex(reading[reading.length - 2]);
        int crcInt = Integer.parseInt(crc, 16);
        return CRC16CAS.check(crcInt, reading[257], reading[256]);
    }

    private void callPrintParameters(final byte[] reading) throws IOException {
        final byte CHECK_BYTE = reading[0];

        if(CHECK_BYTE == 0x39){
            System.out.println("Comando não implementado.");
            writeParametersFile(reading);
        }
        else if(CHECK_BYTE == (byte)0x20 || CHECK_BYTE == (byte)0x21 || CHECK_BYTE == (byte)0x22 || CHECK_BYTE == (byte)0x51){
            ParametersReadingSimpleAnswer parametersReadingSimpleAnswer = new ParametersReadingSimpleAnswer();
            parametersReadingSimpleAnswer.printAnswer(reading);
            writeParametersFile(reading);
        }
        else if(CHECK_BYTE == (byte) 0xC1){
            final String serialnumber = ByteArrayUtils.byteToHex(reading[1]) + ByteArrayUtils.byteToHex(reading[2]) + ByteArrayUtils.byteToHex(reading[3]) + ByteArrayUtils.byteToHex(reading[4]);
            final String subCommand = ByteArrayUtils.byteToHex(reading[5]);
            String protocol;
            String authenticationAlgorithm = null;
            switch (reading[6]){
                case 0x00:
                    protocol = "Monoponto";
                case 0x02:
                    protocol = "Multiponto 2 digitos";
                case 0x08:
                    protocol = "Multiponto 8 digitos";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + reading[6]);
            }
            switch (reading[7]){
                case 0x00:
                    authenticationAlgorithm = "FAB";
                case 0x01:
                    authenticationAlgorithm = "MD5";

            }
            System.out.println("Serial number: " + serialnumber);
            System.out.println("Sub command: " + subCommand);
            System.out.println("Protocol: " + protocol);
            System.out.println("Authentication algorithm: " + authenticationAlgorithm);

            writeParametersFile(reading);
        }
        else if(CHECK_BYTE != (byte) 0x05){
            writeParametersFile(reading);
        }


    }



    private void writeParametersFile(byte[] reading) throws IOException {

        final File file = new File();
        file.fileCreator();

        final String SERIAL_NUMBER = ByteArrayUtils.byteToHex(reading[1]) + ByteArrayUtils.byteToHex(reading[2]) +
                ByteArrayUtils.byteToHex(reading[3]);

        if (writtenLines == 0) {
            file.write("");
            file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                    getArgs()[3] + ",BEGIN,123," + SERIAL_NUMBER + ",ABNT03,FULL,3ELEM");
            writtenLines = writtenLines + 1;
        }
        file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                getArgs()[3] + "," + ByteArrayUtils.byteToHex(reading).replaceAll(" ", ""));
        writtenLines = writtenLines + 1;

        if (reading[5] >= 0x10 && endAnswer) {
            file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                    getArgs()[3] + ",END");

        }


    }
    


}