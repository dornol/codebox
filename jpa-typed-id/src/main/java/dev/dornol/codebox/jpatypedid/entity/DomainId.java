package dev.dornol.codebox.jpatypedid.entity;

import java.util.Objects;

public abstract class DomainId {

    private final Long value;

    protected DomainId(Long value) {
        this.value = value;
    }

    public Long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        DomainId domainId = (DomainId) o;
        return Objects.equals(getValue(), domainId.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

    @Override
    public String toString() {
        return "DomainId{" +
                "value=" + value +
                '}';
    }
}
