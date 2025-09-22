package com.example.personal_backend.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Personel bilgilerini temsil eden JPA Entity sınıfı
 * Veritabanındaki 'personel' tablosuna karşılık gelir
 */
@Entity
@Table(name = "personel")
public class Personel {

    // Birincil anahtar - otomatik artan ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Personelin adı - zorunlu alan, 2-50 karakter arası
    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Ad 2-50 karakter arasında olmalıdır")
    @Column(name = "ad", nullable = false, length = 50)
    private String ad;

    // Personelin soyadı - zorunlu alan, 2-50 karakter arası
    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 karakter arasında olmalıdır")
    @Column(name = "soyad", nullable = false, length = 50)
    private String soyad;

    // Email adresi - zorunlu, unique ve geçerli email formatında olmalı
    @NotBlank(message = "Email alanı boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    // Telefon numarası - zorunlu alan, 10-15 karakter arası
    @NotBlank(message = "Telefon alanı boş olamaz")
    @Size(min = 10, max = 15, message = "Telefon numarası 10-15 karakter arasında olmalıdır")
    @Column(name = "telefon", nullable = false, length = 15)
    private String telefon;

    // Çalıştığı departman - zorunlu alan
    @NotBlank(message = "Departman alanı boş olamaz")
    @Column(name = "departman", nullable = false, length = 100)
    private String departman;

    // İş pozisyonu/ünvanı - zorunlu alan
    @NotBlank(message = "Pozisyon alanı boş olamaz")
    @Column(name = "pozisyon", nullable = false, length = 100)
    private String pozisyon;

    // İşe başlama tarihi - zorunlu alan
    @NotNull(message = "İşe başlama tarihi boş olamaz")
    @Column(name = "ise_baslama_tarihi", nullable = false)
    private LocalDate iseBaslamaTarihi;

    // Maaş bilgisi - opsiyonel alan
    @Column(name = "maas")
    private Double maas;

    // Personelin aktif durumu - varsayılan olarak true
    @Column(name = "aktif", nullable = false)
    private Boolean aktif = true;

    // Kayıt oluşturma zamanı - otomatik olarak set edilir
    @CreationTimestamp
    @Column(name = "olusturma_tarihi", nullable = false, updatable = false)
    private LocalDateTime olusturmaTarihi;

    // Son güncelleme zamanı - otomatik olarak güncellenir
    @UpdateTimestamp
    @Column(name = "guncelleme_tarihi")
    private LocalDateTime guncellemeTarihi;

    // Constructors
    
    /**
     * Varsayılan constructor
     */
    public Personel() {}

    /**
     * Tüm alanları içeren constructor
     * @param ad Personelin adı
     * @param soyad Personelin soyadı
     * @param email Email adresi
     * @param telefon Telefon numarası
     * @param departman Departman bilgisi
     * @param pozisyon İş pozisyonu
     * @param iseBaslamaTarihi İşe başlama tarihi
     * @param maas Maaş bilgisi
     */
    public Personel(String ad, String soyad, String email, String telefon, 
                   String departman, String pozisyon, LocalDate iseBaslamaTarihi, Double maas) {
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
        this.departman = departman;
        this.pozisyon = pozisyon;
        this.iseBaslamaTarihi = iseBaslamaTarihi;
        this.maas = maas;
        this.aktif = true; // Yeni personel varsayılan olarak aktif
    }

    // Getters and Setters
    
    /**
     * @return Personel ID'si
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id Personel ID'si
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Personelin adı
     */
    public String getAd() {
        return ad;
    }

    /**
     * @param ad Personelin adı
     */
    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public String getDepartman() {
        return departman;
    }

    public void setDepartman(String departman) {
        this.departman = departman;
    }

    public String getPozisyon() {
        return pozisyon;
    }

    public void setPozisyon(String pozisyon) {
        this.pozisyon = pozisyon;
    }

    public LocalDate getIseBaslamaTarihi() {
        return iseBaslamaTarihi;
    }

    public void setIseBaslamaTarihi(LocalDate iseBaslamaTarihi) {
        this.iseBaslamaTarihi = iseBaslamaTarihi;
    }

    public Double getMaas() {
        return maas;
    }

    public void setMaas(Double maas) {
        this.maas = maas;
    }

    public Boolean getAktif() {
        return aktif;
    }

    public void setAktif(Boolean aktif) {
        this.aktif = aktif;
    }

    public LocalDateTime getOlusturmaTarihi() {
        return olusturmaTarihi;
    }

    public void setOlusturmaTarihi(LocalDateTime olusturmaTarihi) {
        this.olusturmaTarihi = olusturmaTarihi;
    }

    public LocalDateTime getGuncellemeTarihi() {
        return guncellemeTarihi;
    }

    public void setGuncellemeTarihi(LocalDateTime guncellemeTarihi) {
        this.guncellemeTarihi = guncellemeTarihi;
    }

    @Override
    public String toString() {
        return "Personel{" +
                "id=" + id +
                ", ad='" + ad + '\'' +
                ", soyad='" + soyad + '\'' +
                ", email='" + email + '\'' +
                ", telefon='" + telefon + '\'' +
                ", departman='" + departman + '\'' +
                ", pozisyon='" + pozisyon + '\'' +
                ", iseBaslamaTarihi=" + iseBaslamaTarihi +
                ", maas=" + maas +
                ", aktif=" + aktif +
                '}';
    }
}
