package blockchain.manager;

import akka.actor.typed.ActorRef;
import model.Block;
import model.HashResult;

/**
 * Created on 27.05.2023
 *
 * @author Mykola Horkov
 * <br> mykola.horkov@gmail.com
 */
public class MineBlockCommand implements Command {
    private static final long serialVersionUID = 1L;
    private Block block;
    private ActorRef<HashResult> sender;
    private int difficulty;

    public MineBlockCommand(Block block, ActorRef<HashResult> sender, int difficulty) {
        this.block = block;
        this.sender = sender;
        this.difficulty = difficulty;
    }

    public Block getBlock() {
        return block;
    }

    public ActorRef<HashResult> getSender() {
        return sender;
    }

    public int getDifficulty() {
        return difficulty;
    }
}
