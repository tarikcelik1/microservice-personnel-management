package com.example.personal_backend.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PersonelResponseDTO {

    private Long id;
    private String ad;
    private String soyad;
    private String email;
    private String telefon;
    private String departman;
    private String pozisyon;
    private LocalDate iseBaslamaTarihi;
    private Double maas;
    private Boolean aktif;
    private LocalDateTime olusturmaTarihi;
    private LocalDateTime guncellemeTarihi;

    // Constructors
    public PersonelResponseDTO() {}

    public PersonelResponseDTO(Long id, String ad, String soyad, String email, String telefon,
                              String departman, String pozisyon, LocalDate iseBaslamaTarihi,
                              Double maas, Boolean aktif, LocalDateTime olusturmaTarihi,
                              LocalDateTime guncellemeTarihi) {
        this.id = id;
        this.ad = ad;
        this.soyad = soyad;
        this.email = email;
        this.telefon = telefon;
        this.departman = departman;
        this.pozisyon = pozisyon;
        this.iseBaslamaTarihi = iseBaslamaTarihi;
        this.maas = maas;
        this.aktif = aktif;
        this.olusturmaTarihi = olusturmaTarihi;
        this.guncellemeTarihi = guncellemeTarihi;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAd() {
        return ad;
    }

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
}
