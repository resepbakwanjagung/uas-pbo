package dao;

import entity.Mahasiswa;
import java.util.List;

public interface MahasiswaDAO {

    void insert(Mahasiswa m);

    List<Mahasiswa> findAll();

    Mahasiswa findById(int id);

    void update(Mahasiswa m);

    void delete(int id);
}