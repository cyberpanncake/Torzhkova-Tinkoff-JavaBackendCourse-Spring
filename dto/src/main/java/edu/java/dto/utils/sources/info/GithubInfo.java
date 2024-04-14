package edu.java.dto.utils.sources.info;

public class GithubInfo extends SourceInfo {
    private final String owner;
    private final String repo;

    public GithubInfo(String owner, String repo) {
        this.owner = owner;
        this.repo = repo;
    }

    public String getOwner() {
        return owner;
    }

    public String getRepo() {
        return repo;
    }
}
