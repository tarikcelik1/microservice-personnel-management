package com.example.personal_backend.controller;

import com.example.personal_backend.dto.PersonelCreateDTO;
import com.example.personal_backend.dto.PersonelResponseDTO;
import com.example.personal_backend.dto.PersonelUpdateDTO;
import com.example.personal_backend.service.PersonelService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Personel yönetimi için REST API endpoint'lerini sağlayan controller sınıfı
 * CRUD operasyonları, arama ve raporlama işlemlerini destekler
 */
@RestController
@RequestMapping("/api/personel")
@Tag(name = "Personel Management", description = "Personel CRUD operasyonları")
@CrossOrigin(origins = "*") // React frontend için CORS ayarı
public class PersonelController {

    // Loglama için kullanılan logger instance'ı
    private static final Logger logger = LoggerFactory.getLogger(PersonelController.class);

    // Personel iş mantığını yöneten servis sınıfı
    @Autowired
    private PersonelService personelService;

    /**
     * Tüm aktif personelleri listeler
     * @return Personel listesi
     */
    @GetMapping
    @Operation(summary = "Tüm personelleri getir", description = "Aktif olan tüm personelleri listeler")
    public ResponseEntity<List<PersonelResponseDTO>> getAllPersonel() {
        logger.info("GET /api/personel - Tüm personeller istendi");
        List<PersonelResponseDTO> personelList = personelService.getAllPersonel();
        return ResponseEntity.ok(personelList);
    }

    /**
     * Sayfalama ve sıralama ile personelleri listeler
     * @param page Sayfa numarası (0'dan başlar)
     * @param size Sayfa boyutu
     * @param sortBy Sıralama yapılacak alan
     * @param sortDir Sıralama yönü (asc/desc)
     * @return Sayfalanmış personel listesi
     */
    @GetMapping("/paged")
    @Operation(summary = "Sayfalama ile personelleri getir", description = "Belirtilen sayfa ve boyutta personelleri listeler")
    public ResponseEntity<Page<PersonelResponseDTO>> getAllPersonelPaged(
            @Parameter(description = "Sayfa numarası (0'dan başlar)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sayfa boyutu") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Sıralama alanı") @RequestParam(defaultValue = "ad") String sortBy,
            @Parameter(description = "Sıralama yönü") @RequestParam(defaultValue = "asc") String sortDir) {
        
        logger.info("GET /api/personel/paged - Sayfalama ile personeller istendi: page={}, size={}, sortBy={}, sortDir={}", 
                   page, size, sortBy, sortDir);
        
        // Sıralama yönünü belirle (ascending/descending)
        Sort sort = sortDir.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        // Sayfa bilgilerini oluştur
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PersonelResponseDTO> personelPage = personelService.getAllPersonelPaginated(pageable);
        return ResponseEntity.ok(personelPage);
    }

    /**
     * Belirtilen ID'ye sahip personeli getirir
     * @param id Personel ID'si
     * @return Personel bilgileri
     */
    @GetMapping("/{id}")
    @Operation(summary = "ID ile personel getir", description = "Belirtilen ID'ye sahip personeli getirir")
    public ResponseEntity<PersonelResponseDTO> getPersonelById(
            @Parameter(description = "Personel ID'si") @PathVariable Long id) {
        logger.info("GET /api/personel/{} - Personel ID ile istendi", id);
        PersonelResponseDTO personel = personelService.getPersonelById(id);
        return ResponseEntity.ok(personel);
    }

    /**
     * Yeni personel kaydı oluşturur
     * @param createDTO Oluşturulacak personel bilgileri
     * @return Oluşturulan personel bilgileri
     */
    @PostMapping
    @Operation(summary = "Yeni personel oluştur", description = "Yeni bir personel kaydı oluşturur")
    public ResponseEntity<PersonelResponseDTO> createPersonel(
            @Parameter(description = "Oluşturulacak personel bilgileri") @Valid @RequestBody PersonelCreateDTO createDTO) {
        logger.info("POST /api/personel - Yeni personel oluşturma istendi: {}", createDTO.getEmail());
        PersonelResponseDTO createdPersonel = personelService.createPersonel(createDTO);
        return new ResponseEntity<>(createdPersonel, HttpStatus.CREATED);
    }

    /**
     * Mevcut personel bilgilerini günceller
     * @param id Güncellenecek personel ID'si
     * @param updateDTO Güncellenecek bilgiler
     * @return Güncellenmiş personel bilgileri
     */
    @PutMapping("/{id}")
    @Operation(summary = "Personel güncelle", description = "Mevcut personel bilgilerini günceller")
    public ResponseEntity<PersonelResponseDTO> updatePersonel(
            @Parameter(description = "Güncellenecek personel ID'si") @PathVariable Long id,
            @Parameter(description = "Güncellenecek personel bilgileri") @Valid @RequestBody PersonelUpdateDTO updateDTO) {
        logger.info("PUT /api/personel/{} - Personel güncelleme istendi", id);
        PersonelResponseDTO updatedPersonel = personelService.updatePersonel(id, updateDTO);
        return ResponseEntity.ok(updatedPersonel);
    }

    /**
     * Personeli soft delete yapar (aktif durumunu false yapar)
     * @param id Silinecek personel ID'si
     * @return No content response
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Personel sil", description = "Belirtilen ID'ye sahip personeli siler (soft delete)")
    public ResponseEntity<Void> deletePersonel(
            @Parameter(description = "Silinecek personel ID'si") @PathVariable Long id) {
        logger.info("DELETE /api/personel/{} - Personel silme istendi", id);
        personelService.deletePersonel(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Personel arama işlemi yapar
     * @param query Arama metni
     * @param page Sayfa numarası
     * @param size Sayfa boyutu
     * @return Arama sonuçları
     */
    @GetMapping("/search")
    @Operation(summary = "Personel ara", description = "Ad, soyad, email, departman veya pozisyona göre personel arar")
    public ResponseEntity<Page<PersonelResponseDTO>> searchPersonel(
            @Parameter(description = "Arama metni") @RequestParam String query,
            @Parameter(description = "Sayfa numarası") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Sayfa boyutu") @RequestParam(defaultValue = "10") int size) {
        
        logger.info("GET /api/personel/search - Personel arama istendi: query={}", query);
        Pageable pageable = PageRequest.of(page, size);
        Page<PersonelResponseDTO> searchResults = personelService.searchPersonel(query, pageable);
        return ResponseEntity.ok(searchResults);
    }

    /**
     * Sistemdeki tüm departmanları listeler
     * @return Departman listesi
     */
    @GetMapping("/departmanlar")
    @Operation(summary = "Tüm departmanları getir", description = "Sistemdeki tüm departmanları listeler")
    public ResponseEntity<List<String>> getAllDepartmanlar() {
        logger.info("GET /api/personel/departmanlar - Departmanlar istendi");
        List<String> departmanlar = personelService.getAllDepartmanlar();
        return ResponseEntity.ok(departmanlar);
    }

    /**
     * Sistemdeki tüm pozisyonları listeler
     * @return Pozisyon listesi
     */
    @GetMapping("/pozisyonlar")
    @Operation(summary = "Tüm pozisyonları getir", description = "Sistemdeki tüm pozisyonları listeler")
    public ResponseEntity<List<String>> getAllPozisyonlar() {
        logger.info("GET /api/personel/pozisyonlar - Pozisyonlar istendi");
        List<String> pozisyonlar = personelService.getAllPozisyonlar();
        return ResponseEntity.ok(pozisyonlar);
    }
}
