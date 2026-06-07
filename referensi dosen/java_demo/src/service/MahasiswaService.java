package service;

import dao.MahasiswaDAO;
import entity.Mahasiswa;

import java.util.List;

public class MahasiswaService {

    private final MahasiswaDAO dao;

    public MahasiswaService(MahasiswaDAO dao) {
        this.dao = dao;
    }

    public void tambahMahasiswa(Mahasiswa m) {
        if (m.getNama() == null || m.getNama().isBlank())
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        if (m.getNim() == null || m.getNim().isBlank())
            throw new IllegalArgumentException("NIM tidak boleh kosong");
        if (m.getJurusan() == null || m.getJurusan().isBlank())
            throw new IllegalArgumentException("Jurusan tidak boleh kosong");

        dao.insert(m);
    }

    public List<Mahasiswa> semuaMahasiswa() {
        return dao.findAll();
    }

    public Mahasiswa cariMahasiswa(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("ID harus bernilai positif");

        Mahasiswa m = dao.findById(id);
        if (m == null)
            throw new IllegalArgumentException("Mahasiswa dengan ID " + id + " tidak ditemukan");

        return m;
    }

    public void ubahMahasiswa(Mahasiswa m) {
        if (m.getId() <= 0)
            throw new IllegalArgumentException("ID tidak valid untuk update");
        if (m.getNama() == null || m.getNama().isBlank())
            throw new IllegalArgumentException("Nama tidak boleh kosong");
        if (m.getNim() == null || m.getNim().isBlank())
            throw new IllegalArgumentException("NIM tidak boleh kosong");
        if (m.getJurusan() == null || m.getJurusan().isBlank())
            throw new IllegalArgumentException("Jurusan tidak boleh kosong");

        dao.update(m);
    }

    public void hapusMahasiswa(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("ID harus bernilai positif");

        dao.delete(id);
    }
}
