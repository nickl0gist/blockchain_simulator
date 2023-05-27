package blockchain.worker;

import akka.actor.typed.ActorRef;
import model.Block;

/**
 * Created on 27.05.2023
 *
 * @author Mykola Horkov
 * <br> mykola.horkov@gmail.com
 */
public class Command {
    private Block block;
    private int startNonce;
    private int difficulty;
    private ActorRef<blockchain.manager.Command> controller;

    public Command(Block block, int startNonce, int difficulty, ActorRef<blockchain.manager.Command> controller) {
        this.block = block;
        this.startNonce = startNonce;
        this.difficulty = difficulty;
        this.controller = controller;
    }

    public Block getBlock() {
        return block;
    }

    public int getStartNonce() {
        return startNonce;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public ActorRef<blockchain.manager.Command> getController() {
        return controller;
    }
}
