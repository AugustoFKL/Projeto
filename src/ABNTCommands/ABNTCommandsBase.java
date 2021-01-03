package ABNTCommands;

public interface ABNTCommandsBase {
    byte[] setCommandArray(String readerSerial);
    void printAnswer(final byte[] reading);
}
