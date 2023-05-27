package blockchain.manager;

import model.HashResult;

import java.util.Objects;

/**
 * Created on 27.05.2023
 *
 * @author Mykola Horkov
 * <br> mykola.horkov@gmail.com
 */
public class HashResultCommand implements Command {
    private static final long serialVersionUID = 1L;
    private HashResult hashResult;

    public HashResultCommand(HashResult hashResult) {
        this.hashResult = hashResult;
    }

    public HashResult getHashResult() {
        return hashResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HashResultCommand)) return false;
        HashResultCommand that = (HashResultCommand) o;
        return Objects.equals(getHashResult(), that.getHashResult());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getHashResult());
    }
}
