package com.example.personal_backend.repository;

import com.example.personal_backend.entity.Personel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Personel entity'si için veritabanı erişim katmanı
 * Spring Data JPA repository interface'ini extend eder
 * CRUD operasyonları ve özel sorguları içerir
 */
@Repository
public interface PersonelRepository extends JpaRepository<Personel, Long> {

    /**
     * Email adresine göre personel arar
     * @param email Aranacak email adresi
     * @return Optional<Personel> - Bulunan personel veya empty
     */
    Optional<Personel> findByEmail(String email);
    
    /**
     * Belirtilen email adresinin sistemde olup olmadığını kontrol eder
     * @param email Kontrol edilecek email adresi
     * @return boolean - Email varsa true, yoksa false
     */
    boolean existsByEmail(String email);
    
    /**
     * Belirtilen email adresinin belirtilen ID dışında başka kayıtta olup olmadığını kontrol eder
     * Güncelleme işlemlerinde email benzersizliği kontrolü için kullanılır
     * @param email Kontrol edilecek email adresi
     * @param id Hariç tutulacak personel ID'si
     * @return boolean - Email başka kayıtta varsa true, yoksa false
     */
    boolean existsByEmailAndIdNot(String email, Long id);
    
    /**
     * Tüm aktif personelleri listeler
     * @return List<Personel> - Aktif personel listesi
     */
    List<Personel> findByAktifTrue();
    
    /**
     * Belirtilen departmandaki personelleri listeler
     * @param departman Departman adı
     * @return List<Personel> - Departmandaki personel listesi
     */
    List<Personel> findByDepartman(String departman);
    
    /**
     * Belirtilen pozisyondaki personelleri listeler
     * @param pozisyon Pozisyon adı
     * @return List<Personel> - Pozisyondaki personel listesi
     */
    List<Personel> findByPozisyon(String pozisyon);
    
    /**
     * Sayfalama ile aktif personelleri listeler
     * @param pageable Sayfalama ve sıralama bilgileri
     * @return Page<Personel> - Sayfalanmış aktif personel listesi
     */
    Page<Personel> findByAktifTrue(Pageable pageable);
    
    /**
     * Aktif personeller arasında çoklu alan araması yapar
     * Ad, soyad, email, departman ve pozisyon alanlarında arama yapılır
     * @param searchText Arama metni
     * @param pageable Sayfalama bilgileri
     * @return Page<Personel> - Arama sonuçları
     */
    @Query("SELECT p FROM Personel p WHERE p.aktif = true AND " +
           "(LOWER(p.ad) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +        // Ad alanında arama
           "LOWER(p.soyad) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +       // Soyad alanında arama
           "LOWER(p.email) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +       // Email alanında arama
           "LOWER(p.departman) LIKE LOWER(CONCAT('%', :searchText, '%')) OR " +   // Departman alanında arama
           "LOWER(p.pozisyon) LIKE LOWER(CONCAT('%', :searchText, '%')))")        // Pozisyon alanında arama
    Page<Personel> findBySearchText(@Param("searchText") String searchText, Pageable pageable);
    
    /**
     * Sistemdeki tüm benzersiz departmanları listeler (sadece aktif personellerin)
     * @return List<String> - Alfabetik sıralı departman listesi
     */
    @Query("SELECT DISTINCT p.departman FROM Personel p WHERE p.aktif = true ORDER BY p.departman")
    List<String> findAllDepartmanlar();
    
    /**
     * Sistemdeki tüm benzersiz pozisyonları listeler (sadece aktif personellerin)
     * @return List<String> - Alfabetik sıralı pozisyon listesi
     */
    @Query("SELECT DISTINCT p.pozisyon FROM Personel p WHERE p.aktif = true ORDER BY p.pozisyon")
    List<String> findAllPozisyonlar();
}
