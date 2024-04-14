package edu.java.dto.utils.sources.info;

public class StackoverflowInfo extends SourceInfo {
    private final long questionId;

    public StackoverflowInfo(long questionId) {
        this.questionId = questionId;
    }

    public long getQuestionId() {
        return questionId;
    }
}
