package edu.java.dto.utils.sources.info;

public class GithubInfo extends SourceInfo {
    private final String author;
    private final String repository;

    public GithubInfo(String author, String repository) {
        this.author = author;
        this.repository = repository;
    }

    public String getAuthor() {
        return author;
    }

    public String getRepository() {
        return repository;
    }
}
