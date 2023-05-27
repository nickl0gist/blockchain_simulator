package blockchain.manager;

import akka.actor.typed.ActorRef;
import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Behaviors;
import akka.actor.typed.javadsl.Receive;
import blockchain.worker.WorkerBehavior;
import model.Block;
import model.HashResult;

/**
 * Created on 24.05.2023
 *
 * @author Mykola Horkov
 * <br> mykola.horkov@gmail.com
 */
public class ManagerBehaviour extends AbstractBehavior<Command> {

    public ManagerBehaviour(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create(){
       return Behaviors.setup(ManagerBehaviour::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onMessage(MineBlockCommand.class, message -> {
                    this.sender = message.getSender();
                    this.block = message.getBlock();
                    this.difficulty = message.getDifficulty();
                    for (int i = 0; i < 10; i++) {
                        startNextWorker();
                    }
                    return Behaviors.same();
                }).build();
    }

    private ActorRef<HashResult> sender;
    private Block block;
    private int difficulty;
    private int currentNonce;

    private void startNextWorker(){
        ActorRef<blockchain.worker.Command> worker = getContext().spawn(WorkerBehavior.create(), "worker" + currentNonce);
        worker.tell(new blockchain.worker.Command(block, currentNonce * 1000, difficulty, getContext().getSelf()));
        currentNonce++;
    }

}
