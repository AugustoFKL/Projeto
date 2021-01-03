package ABNTCommands;

public class CommandSelect implements ABNTCommandsBase{

    private final String command;
    CommandSelect(String command){
        this.command = command;
    }

    public byte[] setCommandArray(String readerSerial) {
        switch (command) {
            case "TOD":
            case "0x20":
            case "0x21":
            case "0x22":
            case "0x51":
                ParametersReadingSimpleAnswer parametersReadingSimpleAnswer = new ParametersReadingSimpleAnswer();
                setCommandArray(readerSerial);

        }
    }

    @Override
    public void printAnswer(byte[] reading) {

    }
}

