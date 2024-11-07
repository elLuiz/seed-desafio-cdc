package br.com.elibrary.model.author;

import br.com.elibrary.model.GenericEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_author")
@Getter
public class Author extends GenericEntity {
    @Column(name = "author_name", nullable = false)
    private String name;
    @Column(name = "email", nullable = false)
    @Email
    private String email;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    protected Author() {}

    public Author(String name, String email, String description) {
        this.name = name.trim();
        this.email = email.trim();
        this.description = description.trim();
        this.createdAt = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}