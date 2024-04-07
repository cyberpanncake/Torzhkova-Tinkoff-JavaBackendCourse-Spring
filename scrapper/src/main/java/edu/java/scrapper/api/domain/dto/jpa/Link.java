package edu.java.scrapper.api.domain.dto.jpa;

import edu.java.scrapper.api.domain.dto.utils.UriStringConverter;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Link {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Convert(converter = UriStringConverter.class)
    @NotNull
    @Column(nullable = false, length = Integer.MAX_VALUE)
    private URI url;

    @Column(name = "last_update")
    private OffsetDateTime lastUpdate;

    @Column(name = "last_check")
    private OffsetDateTime lastCheck;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "subscription",
               joinColumns = @JoinColumn(name = "link_id"),
               inverseJoinColumns = @JoinColumn(name = "chat_id"))
    private Set<Chat> chats = new LinkedHashSet<>();

    public Link(URI url, OffsetDateTime createdAt) {
        this.url = url;
        this.lastUpdate = createdAt;
        this.lastCheck = createdAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Link link = (Link) object;
        return Objects.equals(url, link.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }
}
