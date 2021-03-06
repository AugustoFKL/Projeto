package ABNTCommands;

import Util.ByteArrayUtils;

public class MeterParametersReadingSimpleAnswer implements ABNTCommandsBase {

    private final int COMMAND_LENGTH = 66;
    private String command;
    private String readerSerial;

    public String getCommand() {
        return command;
    }

    public String getReaderSerial() {
        return readerSerial;
    }

    public int getCOMMAND_LENGTH() {
        return COMMAND_LENGTH;
    }

    public byte[] setCommandArray(String command, String readerSerial) {
        this.command = command;
        this.readerSerial = readerSerial;
        CRCCommands crcCommands = new CRCCommands();
        byte[] commandArray = new byte[getCOMMAND_LENGTH()];
        if ("0x80".equals(getCommand())) {
            commandArray[0] = (byte) 0x80;
        } else {
            throw new IllegalStateException("Unexpected value: " + getCommand());
        }

        char[] readerSerialArray = getReaderSerial().toCharArray();
        String reader1 = readerSerialArray[0] + String.valueOf(readerSerialArray[1]);
        String reader2 = readerSerialArray[2] + String.valueOf(readerSerialArray[3]);
        String reader3 = readerSerialArray[4] + String.valueOf(readerSerialArray[5]);

        commandArray[1] = Byte.parseByte(reader1, 16);
        commandArray[2] = Byte.parseByte(reader2, 16);
        commandArray[3] = Byte.parseByte(reader3, 16);


        byte[] crc = crcCommands.generateCRC(commandArray, 0, getCOMMAND_LENGTH() - 2);
        commandArray[64] = crc[0];
        commandArray[65] = crc[1];
        return commandArray;
    }

    public void printAnswer(final byte[] reading) {
        final String command = ByteArrayUtils.byteToHex(reading[0]);
        final String[] read = ByteArrayUtils.byteToHex(reading).split(" ");
        final String serialnumber = read[1] + read[2] + read[3] + read[4];
        final String demandPres1 = read[5];
        final String totalsPres1 = read[6];
        final String demandPres2 = read[7];
        final String totalsPres2 = read[8];
        final String demandPres3 = read[9];
        final String totalsPres3 = read[10];

        System.out.println("Command: " + command);
        System.out.println("Series number: " + serialnumber);
        System.out.println("Demand channels 1/2/3: " + demandPres1 + "/" + demandPres2 + "/" + demandPres3);
        System.out.println("Total channels 1/2/3: " + totalsPres1 + "/" + totalsPres2 + "/" + totalsPres3);


    }
}

