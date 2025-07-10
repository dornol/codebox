package dev.dornol.codebox.jpatypedid.converter;

import dev.dornol.codebox.jpatypedid.entity.DomainId;
import org.hibernate.type.descriptor.WrapperOptions;
import org.hibernate.type.descriptor.java.AbstractClassJavaType;
import org.hibernate.type.descriptor.jdbc.BigIntJdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcType;
import org.hibernate.type.descriptor.jdbc.JdbcTypeIndicators;

import java.util.function.Function;

public class EntityIdJavaType<T extends DomainId> extends AbstractClassJavaType<T> {
    protected EntityIdJavaType(Class<? extends T> type, Function<Long, T> factory) {
        super(type);
        this.factory = factory;
    }

    private final transient Function<Long, T> factory;

    @Override
    public JdbcType getRecommendedJdbcType(JdbcTypeIndicators indicators) {
        return BigIntJdbcType.INSTANCE;
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> X unwrap(T value, Class<X> type, WrapperOptions options) {
        if (value == null) {
            return null;
        }

        if (type.isAssignableFrom(Long.class)) {
            return (X) value.getValue(); // 도메인 ID → Long
        }

        if (type.isAssignableFrom(getJavaType())) {
            return (X) value; // 이미 래핑된 상태 그대로 반환
        }

        throw new IllegalArgumentException("Unknown unwrap type: " + type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <X> T wrap(X value, WrapperOptions options) {
        if (value == null) return null;

        if (value instanceof Long longValue) {
            return factory.apply(longValue);
        }

        if (value.getClass().equals(getJavaType())) {
            return (T) value; // 이미 감싼 DomainId 타입이면 그대로 반환
        }

        throw new IllegalArgumentException("Unsupported wrap input: " + value.getClass());
    }
}