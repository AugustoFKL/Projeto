package ABNTCommands;

public class CommandSelect implements ABNTCommandsBase {

    private final String command;
    private boolean haveNextCommand = true;
    private String nextCommand = "0x00";

    public CommandSelect(String command) {
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public boolean haveNextCommand() {
        return haveNextCommand;
    }

    public boolean isCompost(String command) {
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
            case "0x80":
                MeterParametersReadingSimpleAnswer meterParametersReadingSimpleAnswer = new MeterParametersReadingSimpleAnswer();
                return meterParametersReadingSimpleAnswer.setCommandArray(command, readerSerial);
            case "0x26":
            case "0x27":
            case "0x52":
                MassMemoryCountersReading massMemoryCountersReading = new MassMemoryCountersReading();
                return massMemoryCountersReading.setCommandArray(command, readerSerial);
            case "TOT":
                switch (nextCommand) {
                    case "0x00":
                        nextCommand = "0x51";
                        haveNextCommand = true;
                        parametersReadingSimpleAnswer = new ParametersReadingSimpleAnswer();
                        return parametersReadingSimpleAnswer.setCommandArray(nextCommand, readerSerial);
                    case "0x51":
                        nextCommand = "0x80";
                        haveNextCommand = true;
                        meterParametersReadingSimpleAnswer = new MeterParametersReadingSimpleAnswer();
                        return meterParametersReadingSimpleAnswer.setCommandArray(nextCommand, readerSerial);
                    case "0x80":
                        nextCommand = "0x52";
                        haveNextCommand = false;
                        massMemoryCountersReading = new MassMemoryCountersReading();
                        return massMemoryCountersReading.setCommandArray(nextCommand, readerSerial);
                    default:
                        throw new IllegalStateException("Unexpected value: " + nextCommand);
                }

        }
        return new byte[0];
    }

    @Override
    public void printAnswer(byte[] reading) {
        switch (getCommand()) {
            case "TOD":
            case "0x20":
            case "0x21":
            case "0x22":
            case "0x51":
                ParametersReadingSimpleAnswer parametersReadingSimpleAnswer = new ParametersReadingSimpleAnswer();
                parametersReadingSimpleAnswer.printAnswer(reading);
                break;
            case "0x26":
            case "0x27":
            case "0x52":
                MassMemoryCountersReading massMemoryCountersReading = new MassMemoryCountersReading();
                massMemoryCountersReading.printAnswer(reading);
                break;
            case "0x80":
                MeterParametersReadingSimpleAnswer meterParametersReadingSimpleAnswer = new MeterParametersReadingSimpleAnswer();
                meterParametersReadingSimpleAnswer.printAnswer(reading);
                break;
        }
    }
}

