package dev.dornol.codebox.jpatypedid.entity;

public class MemberId extends DomainId {
    protected MemberId(Long value) {
        super(value);
    }

    public static MemberId of(Long value) {
        return new MemberId(value);
    }
}
