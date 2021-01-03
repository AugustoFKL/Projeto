package ABNTCommands;

import Util.ByteArrayUtils;

public class ParametersReadingSimpleAnswer implements ABNTCommandsBase{

    private String command;
    private final int COMMAND_LENGTH = 66;
    private String readerSerial;
    public ParametersReadingSimpleAnswer(){

    }

    public String getCommand() { return command; }

    public byte[] setCommandArray(String command, String readerSerial) {
        this.command = command;
        this.readerSerial = readerSerial;
        byte[] commandArray = new byte[COMMAND_LENGTH];
        commandArray[0] = (byte) Integer.parseInt(command);

        char[] readerSerialArray = readerSerial.toCharArray();
        String reader1 = String.valueOf(readerSerialArray[0]) + String.valueOf(readerSerialArray[1]);
        String reader2 = String.valueOf(readerSerialArray[2]) + String.valueOf(readerSerialArray[3]);
        String reader3 = String.valueOf(readerSerialArray[4]) + String.valueOf(readerSerialArray[5]);

        commandArray[2] = (byte) Integer.parseInt(reader1);
        commandArray[3] = (byte) Integer.parseInt(reader1);
        commandArray[4] = (byte) Integer.parseInt(reader1);
        return commandArray;
    }


    public void printAnswer(final byte[] reading) {
        final String command = ByteArrayUtils.byteToHex(reading[0]);
        final String[] read = ByteArrayUtils.byteToHex(reading).split(" ");

        final String serialnumber = read[1] + read[2] + read[3] + read[4];
        final String actualData = read[5] + ":" + read[6] + ":" + read[7] + " " + read[8] + "/" + read[9] + "/" + read[10];
        final String lastDemandData = read[18] + ":" + read[19] + ":" + read[20] + " " + read[21] + "/" + read[22] + "/" + read[23];
        final String multiplicationsConstantsCh1 = read[128]+read[129]+read[130]+"/"+read[131]+read[132]+read[133];
        final String multiplicationsConstantsCh2 = read[134]+read[135]+read[136]+"/"+read[137]+read[138]+read[139];
        final String multiplicationsConstantsCh3 = read[140]+read[141]+read[142]+"/"+read[143]+read[144]+read[145];
        final String softwareVersion = read[147] + read[148];
        final String meterModel = read[152]+read[153];
        System.out.println("Command: " + command);
        System.out.println("Series number: " + serialnumber);
        System.out.println("Actual data: " + actualData);
        System.out.println("Last demand data: " + lastDemandData);
        System.out.println("Multiplication constants Channel 1: " + multiplicationsConstantsCh1);
        System.out.println("Multiplication constants Channel 2: " + multiplicationsConstantsCh2);
        System.out.println("Multiplication constants Channel 3: " + multiplicationsConstantsCh3);
        System.out.println("Software Version: " + softwareVersion);
        System.out.println("Meter model: " + meterModel);

        if(read[0].equals("51")){
            final String lastMassMemory = read[12] + ":" + read[13] + ":" + read[14] + " " + read[15] + "/" + read[16] + "/" + read[17];
            final String bits12MassMemory = read[74]+read[75]+read[76];
            System.out.println("Last mass memory interval end date: " + lastMassMemory);
            System.out.println("Number of 12 bits words in the mass memory: " + bits12MassMemory);
        }
    }
}
