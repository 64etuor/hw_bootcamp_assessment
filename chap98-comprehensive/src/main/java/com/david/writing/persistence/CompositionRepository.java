package com.david.writing.persistence;

import java.util.ArrayList;
import java.util.List;

import com.david.writing.domain.Composition;

public class CompositionRepository {
    private final CompositionStorage compositionStorage;
    private final List<Composition> compositionList;

    public CompositionRepository(CompositionStorage compositionStorage) {
        this.compositionStorage = compositionStorage;
        this.compositionList = new ArrayList<>(compositionStorage.loadCompositions());
    }

    public void save(Composition composition) {
        compositionList.add(composition);
        compositionStorage.saveCompositions(compositionList);
    }

    public List<Composition> findAll() {
        return new ArrayList<>(compositionList);
    }

    public void deleteAll() {
        compositionList.clear();
        compositionStorage.saveCompositions(compositionList);
    }
} 