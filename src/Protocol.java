import Util.ByteArrayUtils;
import Util.CRC16CAS;

import java.io.IOException;
import java.text.MessageFormat;

class Protocol {
    
    private final String[] args;
    private final ConnectionCommands connectionCommands;
    private int writtenLines = 0;
    private final byte[] commandArray = new byte[66];
    private final long LIMIT_TIME = 20000;

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
                setArray((byte) 0x51);
                sendStarter();
                Thread.sleep(300);
                setArray((byte) 0x80);
                sendStarter();
                Thread.sleep(300);
                setArray((byte) 0x52);
                sendStarter();
                break;
            case "TOD":
                setArray((byte) 0x21);
                sendStarter();
                break;
            case "0xC1":
                setArray((byte) 0xC1);
                sendStarter();
                break;
        }

    }

    private void setArray(byte command){
        commandArray[0] = command;
        commandArray[1] = 0x12;
        commandArray[2] = 0x34;
        commandArray[3] = 0x56;
        if(command == (byte) 0xC1){
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
        final long start = System.currentTimeMillis();
        boolean test = true;

        connectionCommands.sendCommand(commandArray);
        while(System.currentTimeMillis()-start < getLIMIT_TIME() && test){
            if(getConnectionCommands().checkInputStream(257)) {
                byte[] answer = getConnectionCommands().readInputStream();
                test = answer[0] == 0x52;
                System.out.println(checkCRC(answer));
                if(checkCRC(answer)){
                    callPrintParameters(answer);
                }else{
                    System.out.println("Invalid CRC.");
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
            printParametersSimpleCommand(reading);
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
        }
        else if(CHECK_BYTE != (byte) 0x05){
            writeParametersFile(reading);
        }


    }

    private void printParametersSimpleCommand(final byte[] reading) {
        System.out.println(reading.length);        final String command = ByteArrayUtils.byteToHex(reading[0]);
        final String serialnumber = MessageFormat.format("{0}{1}{2}{3}", ByteArrayUtils.byteToHex(reading[1]), ByteArrayUtils.byteToHex(reading[2]), ByteArrayUtils.byteToHex(reading[3]), ByteArrayUtils.byteToHex(reading[4]));
        final String actualData = reading[5] + ":" + reading[6] + ":" + reading[7] + " " + reading[8] + "/" + reading[9] + "/" + reading[10];
        final String lastDemandData = reading[18] + ":" + reading[19] + ":" + reading[20] + " " + reading[21] + "/" + reading[22] + "/" + reading[23];
        String multiplicationsConstantsCh1 = "";
        String multiplicationsConstantsCh2 = "";
        String multiplicationsConstantsCh3 = "";
        final String softwareVersion = ByteArrayUtils.byteToHex(reading[147]) + ByteArrayUtils.byteToHex(reading[148]);
        for (int i = 128; i <= 133; i = i + 1) {
            multiplicationsConstantsCh1 = multiplicationsConstantsCh1.concat(ByteArrayUtils.byteToHex(reading[i]));
            if (i == 130) {
                multiplicationsConstantsCh1 = multiplicationsConstantsCh1.concat("/");
            }
        }
        for (int i = 134; i <= 139; i = i + 1) {
            multiplicationsConstantsCh2 = multiplicationsConstantsCh2.concat(ByteArrayUtils.byteToHex(reading[i]));
            if (i == 136) {
                multiplicationsConstantsCh2 = multiplicationsConstantsCh2.concat("/");
            }
        }
        for (int i = 140; i <= 145; i = i + 1) {
            multiplicationsConstantsCh3 = multiplicationsConstantsCh3.concat(ByteArrayUtils.byteToHex(reading[i]));
            if (i == 142) {
                multiplicationsConstantsCh3 = multiplicationsConstantsCh3.concat("/");
            }
        }

        System.out.println("Multiplication constants Channel 1: " + multiplicationsConstantsCh1);
        System.out.println("Multiplication constants Channel 2: " + multiplicationsConstantsCh2);
        System.out.println("Multiplication constants Channel 3: " + multiplicationsConstantsCh3);
        System.out.println("Software Version: " + softwareVersion);

        System.out.println("Command: " + command);
        System.out.println("Series number: " + serialnumber);
        System.out.println("Actual data: " + actualData);
        System.out.println("Last demand data: " + lastDemandData);

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

        if (reading[5] >= 0x10) {
            file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                    getArgs()[3] + ",END");

        }


    }
    


}