package com.david.persistence;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class FileCompositionStorage implements CompositionStorage {
    private final String FILE_PATH = "src/main/java/com/david/comprehensive/db/compositionDB.dat";


    @Override
    public void saveCompositions(List<Composition> compositions) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(compositions);
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 중 오류 발생", e);
        }
    }

    }
}
