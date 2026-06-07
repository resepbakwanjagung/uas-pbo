package dao;

import entity.Kamar;
import java.util.List;

public interface KamarDAO {
    void insert(Kamar kamar);
    List<Kamar> findAll();
    Kamar findById(int id);
    Kamar findByNomorKamar(String nomorKamar);
    void update(Kamar kamar);
    void delete(int id);
}
