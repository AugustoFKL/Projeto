package ABNTCommands;

import Util.ByteArrayUtils;

public class MassMemoryCountersReading implements ABNTCommandsBase {

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
        switch (getCommand()) {
            case "0x26":
                commandArray[0] = (byte) 0x26;
                break;
            case "0x27":
                commandArray[0] = (byte) 0x27;
                break;
            case "0x52":
                commandArray[0] = (byte) 0x52;
                break;
            default:
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
        final String blockNumber = read[5] + read[6];

        System.out.println("Command: " + command);
        System.out.println("Series number: " + serialnumber);
        System.out.println("Block number: " + blockNumber);


    }
}
