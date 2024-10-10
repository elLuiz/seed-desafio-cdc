package br.com.elibrary.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "tb_author")
@Getter
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "author_name", columnDefinition = "varchar(255)", nullable = false)
    private String name;
    @Column(name = "email", columnDefinition = "varchar(255)", nullable = false)
    @Email
    private String email;
    @Column(name = "description", columnDefinition = "varchar(400)", nullable = false)
    private String description;
    @Column(name = "created_at", columnDefinition = "timestamp with time zone", nullable = false)
    private LocalDateTime createdAt;

    // TODO: Adicionar validações
    public Author(String name, String email, String description) {
        this.name = name;
        this.email = email;
        this.description = description;
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