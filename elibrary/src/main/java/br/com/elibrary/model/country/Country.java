package br.com.elibrary.model.country;

import br.com.elibrary.model.GenericEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Table(name = "tb_country")
@Entity
public class Country extends GenericEntity {
    @Column(name = "name", unique = true)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tb_state", joinColumns = {@JoinColumn(name = "fk_country_id", foreignKey = @ForeignKey(name = "fk_country_id"))})
    private Set<State> states;

    protected Country() {}

    public Country(String name) {
        this.name = name.trim();
    }

    public void addState(State state) {
        if (this.states == null) {
            this.states = new HashSet<>();
        }
        this.states.add(state);
    }

    public Set<State> getStates() {
        return states;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Country country = (Country) o;
        return Objects.equals(name, country.name) && Objects.equals(states, country.states);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, states);
    }
}