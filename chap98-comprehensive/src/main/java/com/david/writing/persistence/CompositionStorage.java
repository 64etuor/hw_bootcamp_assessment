package com.david.writing.persistence;

import java.util.List;
import com.david.writing.domain.Composition;

public interface CompositionStorage {
    void saveCompositions(List<Composition> compositions);
    List<Composition> loadCompositions();
} 