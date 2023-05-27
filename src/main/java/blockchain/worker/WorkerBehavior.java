package blockchain.worker;

import akka.actor.typed.Behavior;
import akka.actor.typed.javadsl.AbstractBehavior;
import akka.actor.typed.javadsl.ActorContext;
import akka.actor.typed.javadsl.Receive;
import akka.actor.typed.javadsl.Behaviors;
import blockchain.manager.HashResultCommand;
import model.HashResult;
import utils.BlockChainUtils;

public class WorkerBehavior extends AbstractBehavior<Command> {

    private WorkerBehavior(ActorContext<Command> context) {
        super(context);
    }

    public static Behavior<Command> create() {
        return Behaviors.setup(WorkerBehavior::new);
    }

    @Override
    public Receive<Command> createReceive() {
        return newReceiveBuilder()
                .onAnyMessage(message -> {
                    String hash = new String(new char[message.getDifficulty()]).replace("\0", "X");
                    String target = new String(new char[message.getDifficulty()]).replace("\0", "0");

                    int nonce = message.getStartNonce();
                    while (!hash.substring(0, message.getDifficulty()).equals(target) && nonce < message.getStartNonce() + 1000) {
                        nonce++;
                        String dataToEncode = message.getBlock().getPreviousHash() + Long.toString(message.getBlock().getTransaction().getTimestamp())
                                + Integer.toString(nonce)
                                + message.getBlock().getTransaction();
                        hash = BlockChainUtils.calculateHash(dataToEncode);
                    }
                    if (hash.substring(0, message.getDifficulty()).equals(target)) {
                        HashResult hashResult = new HashResult();
                        hashResult.foundAHash(hash, nonce);
                        getContext().getLog().debug(hashResult.getNonce() + " : " + hashResult.getHash());
                        message.getController().tell(new HashResultCommand(hashResult));
                        return Behaviors.same();
                    } else {
                        getContext().getLog().debug("null");
                        return Behaviors.same();
                    }
                })
                .build();
    }

}
