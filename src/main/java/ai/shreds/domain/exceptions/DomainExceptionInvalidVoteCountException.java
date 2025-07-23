package ai.shreds.domain.exceptions;

public class DomainExceptionInvalidVoteCountException extends Exception {
    private static final long serialVersionUID = 1L;
    private final Integer voteCount;

    public DomainExceptionInvalidVoteCountException(String message, Integer voteCount) {
        super(message);
        this.voteCount = voteCount;
    }

    public Integer getVoteCount() {
        return voteCount;
    }
}