import akka.actor.testkit.typed.CapturedLogEvent;
import akka.actor.testkit.typed.javadsl.BehaviorTestKit;
import akka.actor.testkit.typed.javadsl.TestInbox;
import blockchain.manager.Command;
import blockchain.manager.HashResultCommand;
import blockchain.worker.WorkerBehavior;
import model.Block;
import model.HashResult;
import org.junit.jupiter.api.Test;
import org.slf4j.event.Level;
import utils.BlocksData;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Created on 21.05.2023
 *
 * @author Mykola Horkov
 * <br> mykola.horkov@gmail.com
 */
public class MiningTests {

    @Test
    void testMiningFailsIfNonceNotInRange(){
        BehaviorTestKit<blockchain.worker.Command> testActor = BehaviorTestKit.create(WorkerBehavior.create());
        Block bLock = BlocksData.getNextBlock(0, "0");
        TestInbox<Command> controller = TestInbox.create();
        blockchain.worker.Command message = new blockchain.worker.Command(bLock, 0, 5, controller.getRef());
        testActor.run(message);
        List<CapturedLogEvent> logMessages = testActor.getAllLogEntries();
        assertEquals(1, logMessages.size());
        assertEquals("null", logMessages.get(0).message());
        assertEquals(Level.DEBUG, logMessages.get(0).level());
    }

    @Test
    void testMiningPassesIfNonceIsInRange(){
        BehaviorTestKit<blockchain.worker.Command> testActor = BehaviorTestKit.create(WorkerBehavior.create());
        Block bLock = BlocksData.getNextBlock(0, "0");
        TestInbox<Command> controller = TestInbox.create();
        blockchain.worker.Command message = new blockchain.worker.Command(bLock, 82700, 5, controller.getRef());
        testActor.run(message);
        List<CapturedLogEvent> logMessages = testActor.getAllLogEntries();
        assertEquals(1, logMessages.size());
        String expected = "82741 : 0000081e9d118bf0827bed8f4a3e142a99a42ef29c8c3d3e24ae2592456c440b";
        assertEquals(expected, logMessages.get(0).message());
        assertEquals(Level.DEBUG, logMessages.get(0).level());
    }

    @Test
    public void messageReceivedIfNonceInRange(){
        BehaviorTestKit<blockchain.worker.Command> testActor = BehaviorTestKit.create(WorkerBehavior.create());
        Block bLock = BlocksData.getNextBlock(0, "0");

        TestInbox<Command> controller = TestInbox.create();

        blockchain.worker.Command message = new blockchain.worker.Command(bLock, 82700, 5, controller.getRef());
        testActor.run(message);

        HashResult expectedHashResult = new HashResult();
        expectedHashResult.foundAHash("0000081e9d118bf0827bed8f4a3e142a99a42ef29c8c3d3e24ae2592456c440b", 82741);

        Command expectedCommand = new HashResultCommand(expectedHashResult);
        controller.expectMessage(expectedCommand);
    }

    @Test
    public void nonMessageReceivedIfNonceNotInRange(){
        BehaviorTestKit<blockchain.worker.Command> testActor = BehaviorTestKit.create(WorkerBehavior.create());
        Block bLock = BlocksData.getNextBlock(0, "0");
        TestInbox<Command> controller = TestInbox.create();
        blockchain.worker.Command message = new blockchain.worker.Command(bLock, 0, 5, controller.getRef());
        testActor.run(message);

        assertFalse(controller.hasMessages());
    }
}
