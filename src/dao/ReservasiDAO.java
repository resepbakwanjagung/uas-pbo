package dao;

import entity.Reservasi;
import java.util.List;

public interface ReservasiDAO {
    void insert(Reservasi reservasi);
    List<Reservasi> findAll();
    Reservasi findById(int id);
    List<Reservasi> findByNamaTamu(String namaTamu);
    void update(Reservasi reservasi);
    void delete(int id);
}
