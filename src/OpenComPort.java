import java.io.IOException;

public class OpenComPort {

    public static void main(String[] args) throws IOException, InterruptedException {


        final ConnectionCommands connectionCommands = new ConnectionCommands("serialPort", args);
        if(connectionCommands.selectConnection()){
            BaseProtocol baseProtocol = new BaseProtocol(args, connectionCommands);
            baseProtocol.commandsStart();
        }
    }
}
