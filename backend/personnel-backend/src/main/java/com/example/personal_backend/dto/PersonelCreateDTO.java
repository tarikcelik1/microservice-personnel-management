package com.example.personal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class PersonelCreateDTO {

    @NotBlank(message = "Ad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Ad 2-50 karakter arasında olmalıdır")
    private String ad;

    @NotBlank(message = "Soyad alanı boş olamaz")
    @Size(min = 2, max = 50, message = "Soyad 2-50 karakter arasında olmalıdır")
    private String soyad;

    @NotBlank(message = "Email alanı boş olamaz")
    @Email(message = "Geçerli bir email adresi giriniz")
    private String email;

    @NotBlank(message = "Telefon alanı boş olamaz")
    @Size(min = 10, max = 15, message = "Telefon numarası 10-15 karakter arasında olmalıdır")
    private String telefon;

    @NotBlank(message = "Departman alanı boş olamaz")
    private String departman;

    @NotBlank(message = "Pozisyon alanı boş olamaz")
    private String pozisyon;

    @NotNull(message = "İşe başlama tarihi boş olamaz")
    private LocalDate iseBaslamaTarihi;

    private Double maas;

    // Constructors
    public PersonelCreateDTO() {}

    // Getters and Setters
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
}
