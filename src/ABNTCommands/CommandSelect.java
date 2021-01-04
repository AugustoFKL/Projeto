package ABNTCommands;

public class CommandSelect implements ABNTCommandsBase{

    private final String command;
    public CommandSelect(String command){
        this.command = command;
    }

    public String getCommand() { return command; }

    public boolean isCompost(String command){
        switch (command) {
            case "0x26":
            case "0x27":
            case "0x52":
            case "TOT":
                return true;
        }
        return false;
    }

    public byte[] setCommandArray(String command, String readerSerial) {
        switch (getCommand()) {
            case "TOD":
            case "0x20":
            case "0x21":
            case "0x22":
            case "0x51":
                ParametersReadingSimpleAnswer parametersReadingSimpleAnswer = new ParametersReadingSimpleAnswer();
                return parametersReadingSimpleAnswer.setCommandArray(command, readerSerial);
        }
        return new byte[0];
    }

    @Override
    public void printAnswer(byte[] reading) {

    }
}

