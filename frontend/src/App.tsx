import React, { useState } from 'react';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import { Toaster } from 'react-hot-toast'; // Toast bildirimler için
import { PersonelProvider } from './context/PersonelContext';
import PersonelList from './components/PersonelList';
import PersonelForm from './components/PersonelForm';
import { PersonelResponse } from './types/personel';
import './index.css';

/**
 * Ana uygulama component'i
 * Tüm sayfa görünümlerini yönetir ve routing mantığını içerir
 */
const App: React.FC = () => {
  // Mevcut görünüm durumunu yönetir (liste, oluştur, düzenle, görüntüle)
  const [currentView, setCurrentView] = useState<'list' | 'create' | 'edit' | 'view'>('list');
  // Seçili personel durumunu yönetir (düzenleme/görüntüleme için)
  const [selectedPersonel, setSelectedPersonel] = useState<PersonelResponse | null>(null);

  /**
   * Yeni personel oluşturma moduna geçer
   */
  const handleCreateNew = () => {
    setSelectedPersonel(null);     // Önceki seçimi temizle
    setCurrentView('create');      // Oluşturma görünümüne geç
  };

  /**
   * Personel düzenleme moduna geçer
   * @param personel - Düzenlenecek personel
   */
  const handleEdit = (personel: PersonelResponse) => {
    setSelectedPersonel(personel); // Düzenlenecek personeli seç
    setCurrentView('edit');        // Düzenleme görünümüne geç
  };

  /**
   * Personel görüntüleme moduna geçer
   * @param personel - Görüntülenecek personel
   */
  const handleView = (personel: PersonelResponse) => {
    setSelectedPersonel(personel); // Görüntülenecek personeli seç
    setCurrentView('view');        // Görüntüleme moduna geç
  };

  /**
   * İşlem başarılı olduğunda liste görünümüne döner
   */
  const handleSuccess = () => {
    setSelectedPersonel(null);     // Seçimi temizle
    setCurrentView('list');        // Liste görünümüne dön
  };

  /**
   * İptal işleminde liste görünümüne döner
   */
  const handleCancel = () => {
    setSelectedPersonel(null);     // Seçimi temizle
    setCurrentView('list');        // Liste görünümüne dön
  };

  /**
   * Mevcut görünüm durumuna göre uygun component'i render eder
   * @returns JSX.Element | null
   */
  const renderContent = () => {
    switch (currentView) {
      case 'create':
      case 'edit':
        // Personel oluşturma/düzenleme formu
        return (
          <PersonelForm
            personel={selectedPersonel}    // Düzenleme için mevcut personel (create'de null)
            onSuccess={handleSuccess}      // Başarı callback'i
            onCancel={handleCancel}        // İptal callback'i
          />
        );
      case 'view':
        // Personel detay görünümü
        return selectedPersonel ? (
          <PersonelDetail
            personel={selectedPersonel}    // Görüntülenecek personel
            onEdit={() => handleEdit(selectedPersonel)} // Düzenleme callback'i
            onClose={handleCancel}         // Kapatma callback'i
          />
        ) : null;
      case 'list':
      default:
        // Personel listesi (varsayılan görünüm)
        return (
          <PersonelList
            onEdit={handleEdit}            // Düzenleme callback'i
            onView={handleView}            // Görüntüleme callback'i
          />
        );
    }
  };

  return (
    // PersonelProvider ile tüm uygulamayı sarmalayarak global state sağlar
    <PersonelProvider>
      <div className="min-h-screen bg-gray-50">
        {/* Toast bildirimleri için container */}
        <Toaster position="top-right" />
        
        {/* Header - Üst navigasyon ve başlık */}
        <header className="bg-white shadow-sm border-b border-gray-200">
          <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
            <div className="flex justify-between items-center h-16">
              {/* Logo/Başlık alanı */}
              <div className="flex items-center">
                <h1 className="text-2xl font-bold text-gray-900">
                  Personel Yönetim Sistemi
                </h1>
              </div>
              {/* Navigasyon butonları */}
              <nav className="flex space-x-4">
                <button
                  onClick={() => setCurrentView('list')}
                  className={`px-3 py-2 rounded-md text-sm font-medium ${
                    currentView === 'list'
                      ? 'bg-primary-100 text-primary-700' // Aktif görünüm stili
                      : 'text-gray-500 hover:text-gray-700' // Normal görünüm stili
                  }`}
                >
                  Personel Listesi
                </button>
                <button
                  onClick={handleCreateNew}
                  className="btn-primary"
                >
                  Yeni Personel Ekle
                </button>
              </nav>
            </div>
          </div>
        </header>

        {/* Main Content - Ana içerik alanı */}
        <main className="max-w-7xl mx-auto py-6 px-4 sm:px-6 lg:px-8">
          {/* Mevcut görünüm durumuna göre component render et */}
          {renderContent()}
        </main>

        {/* Footer - Alt bilgi */}
        <footer className="bg-white border-t border-gray-200 mt-12">
          <div className="max-w-7xl mx-auto py-4 px-4 sm:px-6 lg:px-8">
            <div className="text-center text-sm text-gray-500">
              © 2024 Personel Yönetim Sistemi. Tüm hakları saklıdır.
            </div>
          </div>
        </footer>
      </div>
    </PersonelProvider>
  );
};

/**
 * Personel detay görünümü component'i
 * Seçili personelin tüm bilgilerini görüntüler
 */
interface PersonelDetailProps {
  personel: PersonelResponse;    // Görüntülenecek personel bilgileri
  onEdit: () => void;           // Düzenleme butonuna tıklandığında çalışacak fonksiyon
  onClose: () => void;          // Kapatma butonuna tıklandığında çalışacak fonksiyon
}

const PersonelDetail: React.FC<PersonelDetailProps> = ({ personel, onEdit, onClose }) => {
  return (
    <div className="card">
      {/* Başlık ve aksiyon butonları */}
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          Personel Detayları
        </h2>
        <div className="flex space-x-2">
          {/* Düzenleme butonu */}
          <button
            onClick={onEdit}
            className="btn-primary"
          >
            Düzenle
          </button>
          {/* Kapatma butonu */}
          <button
            onClick={onClose}
            className="btn-secondary"
          >
            Kapat
          </button>
        </div>
      </div>

      {/* İki sütunlu grid layout */}
      <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
        {/* Sol sütun - Kişisel bilgiler */}
        <div>
          <h3 className="text-lg font-medium text-gray-900 mb-4">Kişisel Bilgiler</h3>
          <dl className="space-y-3">
            <div>
              <dt className="text-sm font-medium text-gray-500">Ad Soyad</dt>
              <dd className="text-sm text-gray-900">{personel.ad} {personel.soyad}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Email</dt>
              <dd className="text-sm text-gray-900">{personel.email}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Telefon</dt>
              <dd className="text-sm text-gray-900">{personel.telefon}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Durum</dt>
              <dd>
                {/* Aktiflik durumuna göre renkli badge */}
                <span className={`inline-flex px-2 py-1 text-xs font-semibold rounded-full ${
                  personel.aktif 
                    ? 'bg-green-100 text-green-800'  // Aktif: yeşil
                    : 'bg-red-100 text-red-800'     // Pasif: kırmızı
                }`}>
                  {personel.aktif ? 'Aktif' : 'Pasif'}
                </span>
              </dd>
            </div>
          </dl>
        </div>

        {/* Sağ sütun - İş bilgileri */}
        <div>
          <h3 className="text-lg font-medium text-gray-900 mb-4">İş Bilgileri</h3>
          <dl className="space-y-3">
            <div>
              <dt className="text-sm font-medium text-gray-500">Departman</dt>
              <dd className="text-sm text-gray-900">{personel.departman}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Pozisyon</dt>
              <dd className="text-sm text-gray-900">{personel.pozisyon}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">Maaş</dt>
              {/* Maaşı Türk Lirası formatında göster */}
              <dd className="text-sm text-gray-900">₺{personel.maas.toLocaleString()}</dd>
            </div>
            <div>
              <dt className="text-sm font-medium text-gray-500">İşe Başlama Tarihi</dt>
              {/* Tarihi Türkçe formatında göster */}
              <dd className="text-sm text-gray-900">
                {new Date(personel.iseBaslamaTarihi).toLocaleDateString('tr-TR')}
              </dd>
            </div>
          </dl>
        </div>
      </div>

      {/* Sistem bilgileri (oluşturma tarihi varsa göster) */}
      {personel.olusturmaTarihi && (
        <div className="mt-6 pt-6 border-t border-gray-200">
          <h3 className="text-lg font-medium text-gray-900 mb-4">Sistem Bilgileri</h3>
          <dl className="grid grid-cols-1 md:grid-cols-2 gap-3">
            <div>
              <dt className="text-sm font-medium text-gray-500">Oluşturma Tarihi</dt>
              {/* Oluşturma tarihini tarih ve saat ile göster */}
              <dd className="text-sm text-gray-900">
                {new Date(personel.olusturmaTarihi).toLocaleString('tr-TR')}
              </dd>
            </div>
            {/* Güncelleme tarihi varsa göster */}
            {personel.guncellemeTarihi && (
              <div>
                <dt className="text-sm font-medium text-gray-500">Son Güncelleme</dt>
                <dd className="text-sm text-gray-900">
                  {new Date(personel.guncellemeTarihi).toLocaleString('tr-TR')}
                </dd>
              </div>
            )}
          </dl>
        </div>
      )}
    </div>
  );
};

export default App;
