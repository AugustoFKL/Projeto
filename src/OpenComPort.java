import java.io.IOException;

public class OpenComPort {

    public static void main(String[] args) throws IOException, InterruptedException {


        final ConnectionSelect connectionSelect = new ConnectionSelect("serialPort", args);
        connectionSelect.selectConnectionType();
    }
}
