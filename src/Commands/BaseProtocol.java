package Commands;

import ABNTCommands.CommandSelect;
import Commands.ConnectionCommands.ConnectionCommands;
import Util.ByteArrayUtils;

import java.io.IOException;

public class BaseProtocol {

    private final String[] args;
    private final ConnectionCommands connectionCommands;
    private int writtenLines = 0;
    private byte[] commandArray = new byte[66];
    private final long LIMIT_TIME = 20000;
    private String command;

    private boolean endAnswer = false;

    public String[] getArgs() {
        return args;
    }

    public ConnectionCommands getConnectionCommands() {
        return connectionCommands;
    }

    public int getWrittenLines() {
        return writtenLines;
    }

    public void setWrittenLines(int writtenLines) {
        this.writtenLines = writtenLines;
    }

    public long getLIMIT_TIME() {
        return LIMIT_TIME;
    }

    public String getCommand() {
        return command;
    }

    public BaseProtocol(String[] args, ConnectionCommands connectionCommands) {
        this.args = args;
        this.connectionCommands = connectionCommands;
    }

    public void commandsStart() throws IOException, InterruptedException {
        this.command = args[1];
        CommandSelect commandSelect = new CommandSelect(command);
        while (commandSelect.haveNextCommand()) {
            commandArray = commandSelect.setCommandArray(command, "123456");
            endAnswer = !commandSelect.haveNextCommand();
            sendStarter();
        }


    }

    private void sendStarter() throws IOException, InterruptedException {
        long start = System.currentTimeMillis();
        boolean isCompost = true;
        System.out.println(ByteArrayUtils.byteToHex(commandArray));
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


    private void callPrintParameters(final byte[] reading) throws IOException {
        CommandSelect commandSelect = new CommandSelect(getCommand());
        final byte CHECK_BYTE = reading[0];

        if(CHECK_BYTE == 0x39){
            System.out.println("Comando não implementado.");
            writeParametersFile(reading);
        } else if(CHECK_BYTE == (byte) 0xC1){
            final String serialnumber = ByteArrayUtils.byteToHex(reading[1]) + ByteArrayUtils.byteToHex(reading[2]) + ByteArrayUtils.byteToHex(reading[3]) + ByteArrayUtils.byteToHex(reading[4]);
            final String subCommand = ByteArrayUtils.byteToHex(reading[5]);
            String protocol;
            String authenticationAlgorithm = null;
            switch (reading[6]){
                case 0x00:
                    protocol = "Monoponto";
                    break;
                case 0x02:
                    protocol = "Multiponto 2 digitos";
                    break;
                case 0x08:
                    protocol = "Multiponto 8 digitos";
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + reading[6]);
            }
            switch (reading[7]){
                case 0x00:
                    authenticationAlgorithm = "FAB";
                    break;
                case 0x01:
                    authenticationAlgorithm = "MD5";
                    break;

            }
            System.out.println("Serial number: " + serialnumber);
            System.out.println("Sub command: " + subCommand);
            System.out.println("Commands.BaseProtocol: " + protocol);
            System.out.println("Authentication algorithm: " + authenticationAlgorithm);

            writeParametersFile(reading);
        } else if(CHECK_BYTE != (byte) 0x05){
            commandSelect.printAnswer(reading);
            writeParametersFile(reading);
        }


    }


    private void writeParametersFile(byte[] reading) throws IOException {
        CommandSelect commandSelect = new CommandSelect(getCommand());
        final File file = new File();
        file.fileCreator();

        final String SERIAL_NUMBER = ByteArrayUtils.byteToHex(reading[1]) + ByteArrayUtils.byteToHex(reading[2]) +
                ByteArrayUtils.byteToHex(reading[3]);

        if (getWrittenLines() == 0) {
            file.write("");
            file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                    getArgs()[3] + ",BEGIN,123," + SERIAL_NUMBER + ",ABNT03,FULL,3ELEM");
            setWrittenLines(getWrittenLines() + 1);
        }
        file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                getArgs()[3] + "," + ByteArrayUtils.byteToHex(reading).replaceAll(" ", ""));
        setWrittenLines(getWrittenLines() + 1);


        if (endAnswer) {
            if (commandSelect.isCompost(getCommand())) {
                if (reading[5] >= 10) {
                    file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                            getArgs()[3] + ",END");
                }
            } else {
                file.write(getArgs()[0] + "," + getArgs()[1] + "," + getArgs()[2] + "," +
                        getArgs()[3] + ",END");
            }
        }


    }


}