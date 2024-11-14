package br.com.elibrary.model.country;

import br.com.elibrary.model.GenericEntity;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

@Table(name = "tb_country")
@Entity
public class Country extends GenericEntity {
    @Column(name = "name", unique = true)
    private String name;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tb_state", joinColumns = {@JoinColumn(name = "fk_country_id", foreignKey = @ForeignKey(name = "fk_country_id"))})
    @OrderColumn
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

    /**
     * <pre>Returns the state object by filtering the registered states by name.</pre>
     * <pre>If the country does not have any state, an empty optional is returned.</pre>
     * <pre>Otherwise, an exception is thrown (i.e., the given state name does not exist and the country has at least one state)</pre>
     * @param stateName The state name.
     * @param throwableSupplier The supplier with the exception to be thrown if the precondition is not met.
     * @return The optional with the state, or empty if the country does not have any state.
     * @param <X> The type of the exception to be thrown.
     */
    public <X extends RuntimeException> Optional<State> getStateOrElse(String stateName, Supplier<X> throwableSupplier) {
        if (this.contains(stateName)) {
            return states.stream().filter(state -> state.equals(new State(stateName))).findFirst();
        } else if (!hasAnyState()) {
            return Optional.empty();
        }
        throw throwableSupplier.get();
    }

    boolean contains(String stateName) {
        return hasAnyState() && this.states.contains(new State(stateName));
    }

    boolean hasAnyState() {
        return this.states != null && !this.states.isEmpty();
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