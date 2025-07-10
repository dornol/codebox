package dev.dornol.codebox.jpatypedid.converter;

import dev.dornol.codebox.jpatypedid.entity.MemberId;

public class MemberIdJavaType extends EntityIdJavaType<MemberId> {
    protected MemberIdJavaType() {
        super(MemberId.class, MemberId::of);
    }
}
