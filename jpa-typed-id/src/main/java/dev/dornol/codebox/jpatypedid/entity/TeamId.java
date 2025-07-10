package dev.dornol.codebox.jpatypedid.entity;

public class TeamId extends DomainId {
    protected TeamId(Long value) {
        super(value);
    }

    public static TeamId of(Long value) {
        return new TeamId(value);
    }
}
