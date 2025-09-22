package com.example.personal_backend.service;

import com.example.personal_backend.dto.*;
import com.example.personal_backend.entity.Personel;
import com.example.personal_backend.exception.PersonelNotFoundException;
import com.example.personal_backend.exception.DuplicateEmailException;
import com.example.personal_backend.repository.PersonelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Personel iş mantığını yöneten servis sınıfı
 * CRUD operasyonları, arama ve bildirim gönderme işlemlerini gerçekleştirir
 */
@Service
@Transactional // Sınıf seviyesinde transaction yönetimi
public class PersonelService {

    // Loglama için kullanılan logger instance'ı
    private static final Logger logger = LoggerFactory.getLogger(PersonelService.class);

    // Veritabanı işlemleri için repository
    @Autowired
    private PersonelRepository personelRepository;

    // Bildirim gönderme işlemleri için servis
    @Autowired
    private NotificationService notificationService;

    /**
     * Tüm aktif personelleri listeler
     * @return Aktif personellerin DTO listesi
     */
    @Transactional(readOnly = true) // Sadece okuma işlemi
    public List<PersonelResponseDTO> getAllPersonel() {
        logger.info("Tüm aktif personeller getiriliyor");
        List<Personel> personelList = personelRepository.findByAktifTrue();
        // Entity'leri DTO'lara dönüştür
        return personelList.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Sayfalama ve sıralama ile personelleri listeler
     * @param pageable Sayfalama ve sıralama bilgileri
     * @return Sayfalanmış personel DTO'ları
     */
    @Transactional(readOnly = true)
    public Page<PersonelResponseDTO> getAllPersonelPaginated(Pageable pageable) {
        logger.info("Sayfalama ile personeller getiriliyor: sayfa={}, boyut={}", 
                   pageable.getPageNumber(), pageable.getPageSize());
        Page<Personel> personelPage = personelRepository.findByAktifTrue(pageable);
        // Page içindeki her Personel entity'sini DTO'ya dönüştür
        return personelPage.map(this::convertToResponseDTO);
    }

    /**
     * Belirtilen ID'ye sahip personeli getirir
     * @param id Personel ID'si
     * @return Personel DTO'su
     * @throws PersonelNotFoundException Personel bulunamazsa
     */
    @Transactional(readOnly = true)
    public PersonelResponseDTO getPersonelById(Long id) {
        logger.info("ID ile personel getiriliyor: {}", id);
        Personel personel = personelRepository.findById(id)
                .orElseThrow(() -> new PersonelNotFoundException("ID: " + id + " ile personel bulunamadı"));
        return convertToResponseDTO(personel);
    }

    /**
     * Yeni personel kaydı oluşturur
     * @param createDTO Oluşturulacak personel bilgileri
     * @return Oluşturulan personel DTO'su
     * @throws DuplicateEmailException Email zaten kullanımdaysa
     */
    public PersonelResponseDTO createPersonel(PersonelCreateDTO createDTO) {
        logger.info("Yeni personel oluşturuluyor: {}", createDTO.getEmail());

        // Email benzersizlik kontrolü
        if (personelRepository.existsByEmail(createDTO.getEmail())) {
            throw new DuplicateEmailException("Bu email adresi zaten kullanımda: " + createDTO.getEmail());
        }

        // DTO'yu Entity'e dönüştür ve kaydet
        Personel personel = convertToEntity(createDTO);
        Personel savedPersonel = personelRepository.save(personel);

        logger.info("Personel başarıyla oluşturuldu: ID={}", savedPersonel.getId());

        // Yeni personel eklendi bildirimi gönder
        PersonelNotificationDTO notification = new PersonelNotificationDTO(
                savedPersonel.getId(),
                savedPersonel.getAd(),
                savedPersonel.getSoyad(),
                savedPersonel.getEmail(),
                "CREATE",
                "Yeni personel eklendi"
        );
        notificationService.sendPersonelNotification(notification);

        return convertToResponseDTO(savedPersonel);
    }

    /**
     * Mevcut personel bilgilerini günceller
     * @param id Güncellenecek personel ID'si
     * @param updateDTO Güncellenecek bilgiler
     * @return Güncellenmiş personel DTO'su
     * @throws PersonelNotFoundException Personel bulunamazsa
     * @throws DuplicateEmailException Email zaten kullanımdaysa
     */
    public PersonelResponseDTO updatePersonel(Long id, PersonelUpdateDTO updateDTO) {
        logger.info("Personel güncelleniyor: ID={}", id);

        // Mevcut personeli bul
        Personel existingPersonel = personelRepository.findById(id)
                .orElseThrow(() -> new PersonelNotFoundException("ID: " + id + " ile personel bulunamadı"));

        // Email benzersizlik kontrolü (kendisi hariç)
        if (updateDTO.getEmail() != null && 
            personelRepository.existsByEmailAndIdNot(updateDTO.getEmail(), id)) {
            throw new DuplicateEmailException("Bu email adresi zaten kullanımda: " + updateDTO.getEmail());
        }

        // Değişen alanları takip et (bildirim için)
        StringBuilder changedFields = new StringBuilder();
        
        // Her alanı kontrol et ve değiştiyse güncelle
        if (updateDTO.getAd() != null && !updateDTO.getAd().equals(existingPersonel.getAd())) {
            existingPersonel.setAd(updateDTO.getAd());
            changedFields.append("Ad, ");
        }
        if (updateDTO.getSoyad() != null && !updateDTO.getSoyad().equals(existingPersonel.getSoyad())) {
            existingPersonel.setSoyad(updateDTO.getSoyad());
            changedFields.append("Soyad, ");
        }
        if (updateDTO.getEmail() != null && !updateDTO.getEmail().equals(existingPersonel.getEmail())) {
            existingPersonel.setEmail(updateDTO.getEmail());
            changedFields.append("Email, ");
        }
        if (updateDTO.getTelefon() != null && !updateDTO.getTelefon().equals(existingPersonel.getTelefon())) {
            existingPersonel.setTelefon(updateDTO.getTelefon());
            changedFields.append("Telefon, ");
        }
        if (updateDTO.getDepartman() != null && !updateDTO.getDepartman().equals(existingPersonel.getDepartman())) {
            existingPersonel.setDepartman(updateDTO.getDepartman());
            changedFields.append("Departman, ");
        }
        if (updateDTO.getPozisyon() != null && !updateDTO.getPozisyon().equals(existingPersonel.getPozisyon())) {
            existingPersonel.setPozisyon(updateDTO.getPozisyon());
            changedFields.append("Pozisyon, ");
        }
        if (updateDTO.getIseBaslamaTarihi() != null && !updateDTO.getIseBaslamaTarihi().equals(existingPersonel.getIseBaslamaTarihi())) {
            existingPersonel.setIseBaslamaTarihi(updateDTO.getIseBaslamaTarihi());
            changedFields.append("İşe Başlama Tarihi, ");
        }
        if (updateDTO.getMaas() != null && !updateDTO.getMaas().equals(existingPersonel.getMaas())) {
            existingPersonel.setMaas(updateDTO.getMaas());
            changedFields.append("Maaş, ");
        }
        if (updateDTO.getAktif() != null && !updateDTO.getAktif().equals(existingPersonel.getAktif())) {
            existingPersonel.setAktif(updateDTO.getAktif());
            changedFields.append("Aktiflik Durumu, ");
        }

        // Güncellenmiş personeli kaydet
        Personel savedPersonel = personelRepository.save(existingPersonel);
        logger.info("Personel başarıyla güncellendi: ID={}", savedPersonel.getId());

        // Eğer değişiklik varsa bildirim gönder
        if (changedFields.length() > 0) {
            String changes = changedFields.toString();
            // Son virgülü kaldır
            if (changes.endsWith(", ")) {
                changes = changes.substring(0, changes.length() - 2);
            }

            PersonelNotificationDTO notification = new PersonelNotificationDTO(
                    savedPersonel.getId(),
                    savedPersonel.getAd(),
                    savedPersonel.getSoyad(),
                    savedPersonel.getEmail(),
                    "UPDATE",
                    "Güncellenen alanlar: " + changes
            );
            notificationService.sendPersonelNotification(notification);
        }

        return convertToResponseDTO(savedPersonel);
    }

    /**
     * Personeli soft delete yapar (aktif durumunu false yapar)
     * @param id Silinecek personel ID'si
     * @throws PersonelNotFoundException Personel bulunamazsa
     */
    public void deletePersonel(Long id) {
        logger.info("Personel siliniyor: ID={}", id);

        Personel personel = personelRepository.findById(id)
                .orElseThrow(() -> new PersonelNotFoundException("ID: " + id + " ile personel bulunamadı"));

        // Soft delete - kaydı fiziksel olarak silmez, sadece aktif durumunu false yapar
        personel.setAktif(false);
        personelRepository.save(personel);

        logger.info("Personel başarıyla silindi (soft delete): ID={}", id);

        // Silme bildirimi gönder
        PersonelNotificationDTO notification = new PersonelNotificationDTO(
                personel.getId(),
                personel.getAd(),
                personel.getSoyad(),
                personel.getEmail(),
                "DELETE",
                "Personel silindi"
        );
        notificationService.sendPersonelNotification(notification);
    }

    /**
     * Personel arama işlemi yapar
     * @param searchText Arama metni
     * @param pageable Sayfalama bilgileri
     * @return Arama sonuçları
     */
    @Transactional(readOnly = true)
    public Page<PersonelResponseDTO> searchPersonel(String searchText, Pageable pageable) {
        logger.info("Personel aranıyor: searchText={}", searchText);
        // Repository'de tanımlı custom query ile arama yap
        Page<Personel> personelPage = personelRepository.findBySearchText(searchText, pageable);
        return personelPage.map(this::convertToResponseDTO);
    }

    /**
     * Sistemdeki tüm departmanları listeler
     * @return Benzersiz departman listesi
     */
    @Transactional(readOnly = true)
    public List<String> getAllDepartmanlar() {
        return personelRepository.findAllDepartmanlar();
    }

    /**
     * Sistemdeki tüm pozisyonları listeler
     * @return Benzersiz pozisyon listesi
     */
    @Transactional(readOnly = true)
    public List<String> getAllPozisyonlar() {
        return personelRepository.findAllPozisyonlar();
    }

    // Converter methods - Entity ve DTO arasında dönüşüm yapar
    
    /**
     * Personel Entity'sini ResponseDTO'ya dönüştürür
     * @param personel Personel entity'si
     * @return PersonelResponseDTO
     */
    private PersonelResponseDTO convertToResponseDTO(Personel personel) {
        return new PersonelResponseDTO(
                personel.getId(),
                personel.getAd(),
                personel.getSoyad(),
                personel.getEmail(),
                personel.getTelefon(),
                personel.getDepartman(),
                personel.getPozisyon(),
                personel.getIseBaslamaTarihi(),
                personel.getMaas(),
                personel.getAktif(),
                personel.getOlusturmaTarihi(),
                personel.getGuncellemeTarihi()
        );
    }

    /**
     * PersonelCreateDTO'yu Personel Entity'sine dönüştürür
     * @param createDTO Oluşturma DTO'su
     * @return Personel entity'si
     */
    private Personel convertToEntity(PersonelCreateDTO createDTO) {
        return new Personel(
                createDTO.getAd(),
                createDTO.getSoyad(),
                createDTO.getEmail(),
                createDTO.getTelefon(),
                createDTO.getDepartman(),
                createDTO.getPozisyon(),
                createDTO.getIseBaslamaTarihi(),
                createDTO.getMaas()
        );
    }
}
