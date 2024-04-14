package edu.java.scrapper.configuration;

import edu.java.dto.utils.LinkParser;
import edu.java.dto.utils.sources.parsers.GithubParser;
import edu.java.dto.utils.sources.parsers.SourceParser;
import edu.java.dto.utils.sources.parsers.StackoverflowParser;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LinkParserConfig {

    @Bean
    public LinkParser linkParser() {
        return new LinkParser(SourceParser.buildChain(Set.of(
            new GithubParser(),
            new StackoverflowParser()
        )), false);
    }
}
