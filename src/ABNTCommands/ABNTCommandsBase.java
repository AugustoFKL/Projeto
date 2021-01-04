package ABNTCommands;

public interface ABNTCommandsBase {
    byte[] setCommandArray(String command, String readerSerial);
    void printAnswer(final byte[] reading);
}
