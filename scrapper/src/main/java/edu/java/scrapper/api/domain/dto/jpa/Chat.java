package edu.java.scrapper.api.domain.dto.jpa;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
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
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;

    @Column(name = "tg_id", nullable = false)
    private Long tgId;

    @ManyToMany(mappedBy = "chats", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Link> links =
        new LinkedHashSet<>();

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || getClass() != object.getClass()) {
            return false;
        }
        Chat chat = (Chat) object;
        return Objects.equals(tgId, chat.tgId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tgId);
    }
}
