import com.bono.api.*;

import java.util.Iterator;

/**
 * Created by hendriknieuwenhuis on 23/04/16.
 */
public class TestEndpoint {

    final String HOST = "192.168.2.4";
    final int PORT = 6600;

    DBExecutor dbExecutor = new DBExecutor(new Config(HOST, PORT));
    PlayerControl playerControl = new PlayerControl(dbExecutor);

    public TestEndpoint() {
        Thread thread = new Thread(new Idle());
        thread.start();

        String reply = "";
        try {
            reply = playerControl.stop();
            print(reply);
            reply = playerControl.play();
            print(reply);

            CommandList commandList;
            commandList = CommandList.commandList(CommandList.COMMAND_LIST_BEGIN);
            DefaultCommand defaultCommand;

            defaultCommand = new DefaultCommand(PlayerControl.PAUSE, "1");
            commandList.addCommand(defaultCommand);
            defaultCommand = new DefaultCommand(PlayerControl.PAUSE, "0");
            commandList.addCommand(defaultCommand);
            defaultCommand = new DefaultCommand(PlayerControl.PAUSE, "1");
            commandList.addCommand(defaultCommand);
            defaultCommand = new DefaultCommand(PlayerControl.PAUSE, "0");
            commandList.addCommand(defaultCommand);
            commandList.commandlistEnd();
            reply = dbExecutor.executeList(commandList);
            //print("Hallo wereld: " + reply);

        } catch (ACKException ack) {
            ack.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    private void print(String print) {
        System.out.println(print);
    }

    private class Idle implements Runnable {

        Endpoint endpoint = new Endpoint(HOST, PORT);

        @Override
        public void run() {
            String reply = "";
            while (true) {
                try {
                    reply = endpoint.command(new DefaultCommand(StatusControl.IDLE));
                } catch (ACKException ack) {
                    ack.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(getClass().getName() + " " + reply);
            }
        }
    }

    public static void main(String[] args) {
        new TestEndpoint();
    }
}