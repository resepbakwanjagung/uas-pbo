package service;

import dao.KamarDAO;
import entity.Kamar;

import java.math.BigDecimal;
import java.util.List;

public class KamarService {

    private final KamarDAO kamarDAO;

    public KamarService(KamarDAO kamarDAO) {
        this.kamarDAO = kamarDAO;
    }

    public void tambahKamar(Kamar kamar) {
        validateKamar(kamar);
        
        // Cek keunikan nomor kamar
        Kamar existing = kamarDAO.findByNomorKamar(kamar.getNomorKamar());
        if (existing != null) {
            throw new IllegalArgumentException("Nomor kamar '" + kamar.getNomorKamar() + "' sudah digunakan.");
        }

        kamarDAO.insert(kamar);
    }

    public List<Kamar> semuaKamar() {
        return kamarDAO.findAll();
    }

    public Kamar cariKamarById(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID kamar tidak valid.");
        }
        Kamar kamar = kamarDAO.findById(id);
        if (kamar == null) {
            throw new IllegalArgumentException("Kamar dengan ID " + id + " tidak ditemukan.");
        }
        return kamar;
    }

    public Kamar cariKamarByNomor(String nomorKamar) {
        if (nomorKamar == null || nomorKamar.isBlank()) {
            throw new IllegalArgumentException("Nomor kamar tidak boleh kosong.");
        }
        Kamar kamar = kamarDAO.findByNomorKamar(nomorKamar);
        if (kamar == null) {
            throw new IllegalArgumentException("Kamar dengan nomor " + nomorKamar + " tidak ditemukan.");
        }
        return kamar;
    }

    public void ubahKamar(Kamar kamar) {
        if (kamar.getId() <= 0) {
            throw new IllegalArgumentException("ID kamar tidak valid untuk diupdate.");
        }
        validateKamar(kamar);

        // Cek jika nomor kamar dirubah ke nomor kamar lain yang sudah ada
        Kamar existing = kamarDAO.findByNomorKamar(kamar.getNomorKamar());
        if (existing != null && existing.getId() != kamar.getId()) {
            throw new IllegalArgumentException("Nomor kamar '" + kamar.getNomorKamar() + "' sudah digunakan oleh kamar lain.");
        }

        kamarDAO.update(kamar);
    }

    public void hapusKamar(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException("ID kamar tidak valid.");
        }
        // Pastikan kamar ada sebelum dihapus
        kamarDAO.findById(id); 
        kamarDAO.delete(id);
    }

    // Validasi Bisnis Kamar
    private void validateKamar(Kamar kamar) {
        if (kamar.getNomorKamar() == null || kamar.getNomorKamar().isBlank()) {
            throw new IllegalArgumentException("Nomor kamar tidak boleh kosong.");
        }
        if (kamar.getTipeKamar() == null || kamar.getTipeKamar().isBlank()) {
            throw new IllegalArgumentException("Tipe kamar tidak boleh kosong.");
        }
        if (kamar.getHargaPerMalam() == null || kamar.getHargaPerMalam().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Harga per malam harus lebih dari 0.");
        }
        
        String status = kamar.getStatus();
        if (status == null || (!status.equals("TERSEDIA") && !status.equals("TERISI") && !status.equals("MAINTENANCE"))) {
            throw new IllegalArgumentException("Status kamar tidak valid. Harus TERSEDIA, TERISI, atau MAINTENANCE.");
        }
    }
}
