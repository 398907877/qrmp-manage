package com.yanyan.core.sml;

import com.yanyan.core.sml.internal.bind.TypeAdapter;
import com.google.gson.reflect.TypeToken;

/**
 * User: Saintcy
 * Date: 2015/4/19
 * Time: 9:09
 */
public interface TypeAdapterFactory {
    /**
     * Returns a type adapter for {@code type}, or null if this factory doesn't
     * support {@code type}.
     */
    <T> TypeAdapter<T> create(Sml sml, TypeToken<T> type);
}
