import React, { useState, useEffect } from 'react';
import { useForm } from 'react-hook-form';
import { PersonelCreateDTO, PersonelResponse } from '../types/personel';
import { personelService } from '../services/personelService';
import { usePersonel } from '../context/PersonelContext';
import toast from 'react-hot-toast';

interface PersonelFormProps {
  personel?: PersonelResponse | null;
  onSuccess: () => void;
  onCancel: () => void;
}

const PersonelForm: React.FC<PersonelFormProps> = ({ personel, onSuccess, onCancel }) => {
  const { dispatch } = usePersonel();
  const [isSubmitting, setIsSubmitting] = useState(false);

  const departmanlar = [
    'İnsan Kaynakları', 'Bilgi İşlem', 'Muhasebe', 'Pazarlama', 
    'Satış', 'Üretim', 'Kalite Kontrol', 'Lojistik'
  ];

  const {
    register,
    handleSubmit,
    formState: { errors },
    reset,
    setValue
  } = useForm<PersonelCreateDTO>();

  useEffect(() => {
    if (personel) {
      setValue('ad', personel.ad);
      setValue('soyad', personel.soyad);
      setValue('email', personel.email);
      setValue('telefon', personel.telefon);
      setValue('departman', personel.departman);
      setValue('pozisyon', personel.pozisyon);
      setValue('maas', personel.maas);
      setValue('iseBaslamaTarihi', personel.iseBaslamaTarihi);
      setValue('aktif', personel.aktif);
    }
  }, [personel, setValue]);

  const onSubmit = async (data: PersonelCreateDTO) => {
    setIsSubmitting(true);
    try {
      let result: PersonelResponse;
      
      if (personel) {
        // Güncelleme
        result = await personelService.update(personel.id!, data);
        dispatch({ type: 'UPDATE_PERSONEL', payload: result });
        toast.success('Personel başarıyla güncellendi');
      } else {
        // Yeni ekleme
        result = await personelService.create(data);
        dispatch({ type: 'ADD_PERSONEL', payload: result });
        toast.success('Personel başarıyla eklendi');
      }
      
      onSuccess();
    } catch (error: any) {
      const errorMessage = error.response?.data?.message || 'İşlem sırasında hata oluştu';
      toast.error(errorMessage);
      dispatch({ type: 'SET_ERROR', payload: errorMessage });
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleCancel = () => {
    reset();
    onCancel();
  };

  return (
    <div className="card">
      <div className="flex justify-between items-center mb-6">
        <h2 className="text-xl font-semibold text-gray-800">
          {personel ? 'Personel Düzenle' : 'Yeni Personel Ekle'}
        </h2>
        <button
          onClick={handleCancel}
          className="text-gray-400 hover:text-gray-600"
        >
          <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
            <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
          </svg>
        </button>
      </div>

      <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Ad */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Ad *
            </label>
            <input
              type="text"
              {...register('ad', { 
                required: 'Ad zorunludur',
                minLength: { value: 2, message: 'Ad en az 2 karakter olmalıdır' }
              })}
              className={`input-field ${errors.ad ? 'border-red-500' : ''}`}
              placeholder="Adı girin"
            />
            {errors.ad && (
              <p className="mt-1 text-sm text-red-600">{errors.ad.message}</p>
            )}
          </div>

          {/* Soyad */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Soyad *
            </label>
            <input
              type="text"
              {...register('soyad', { 
                required: 'Soyad zorunludur',
                minLength: { value: 2, message: 'Soyad en az 2 karakter olmalıdır' }
              })}
              className={`input-field ${errors.soyad ? 'border-red-500' : ''}`}
              placeholder="Soyadı girin"
            />
            {errors.soyad && (
              <p className="mt-1 text-sm text-red-600">{errors.soyad.message}</p>
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Email */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Email *
            </label>
            <input
              type="email"
              {...register('email', { 
                required: 'Email zorunludur',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: 'Geçerli bir email adresi girin'
                }
              })}
              className={`input-field ${errors.email ? 'border-red-500' : ''}`}
              placeholder="email@example.com"
            />
            {errors.email && (
              <p className="mt-1 text-sm text-red-600">{errors.email.message}</p>
            )}
          </div>

          {/* Telefon */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Telefon *
            </label>
            <input
              type="tel"
              {...register('telefon', { 
                required: 'Telefon zorunludur',
                pattern: {
                  value: /^[0-9+\-\s()]+$/,
                  message: 'Geçerli bir telefon numarası girin'
                }
              })}
              className={`input-field ${errors.telefon ? 'border-red-500' : ''}`}
              placeholder="+90 555 123 4567"
            />
            {errors.telefon && (
              <p className="mt-1 text-sm text-red-600">{errors.telefon.message}</p>
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Departman */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Departman *
            </label>
            <select
              {...register('departman', { required: 'Departman zorunludur' })}
              className={`input-field ${errors.departman ? 'border-red-500' : ''}`}
            >
              <option value="">Departman seçin</option>
              {departmanlar.map(dept => (
                <option key={dept} value={dept}>{dept}</option>
              ))}
            </select>
            {errors.departman && (
              <p className="mt-1 text-sm text-red-600">{errors.departman.message}</p>
            )}
          </div>

          {/* Pozisyon */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Pozisyon *
            </label>
            <input
              type="text"
              {...register('pozisyon', { 
                required: 'Pozisyon zorunludur',
                minLength: { value: 2, message: 'Pozisyon en az 2 karakter olmalıdır' }
              })}
              className={`input-field ${errors.pozisyon ? 'border-red-500' : ''}`}
              placeholder="Yazılım Geliştirici"
            />
            {errors.pozisyon && (
              <p className="mt-1 text-sm text-red-600">{errors.pozisyon.message}</p>
            )}
          </div>
        </div>

        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {/* Maaş */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              Maaş (₺) *
            </label>
            <input
              type="number"
              {...register('maas', { 
                required: 'Maaş zorunludur',
                min: { value: 0, message: 'Maaş 0\'dan büyük olmalıdır' }
              })}
              className={`input-field ${errors.maas ? 'border-red-500' : ''}`}
              placeholder="15000"
            />
            {errors.maas && (
              <p className="mt-1 text-sm text-red-600">{errors.maas.message}</p>
            )}
          </div>

          {/* İşe Başlama Tarihi */}
          <div>
            <label className="block text-sm font-medium text-gray-700 mb-1">
              İşe Başlama Tarihi *
            </label>
            <input
              type="date"
              {...register('iseBaslamaTarihi', { required: 'İşe başlama tarihi zorunludur' })}
              className={`input-field ${errors.iseBaslamaTarihi ? 'border-red-500' : ''}`}
            />
            {errors.iseBaslamaTarihi && (
              <p className="mt-1 text-sm text-red-600">{errors.iseBaslamaTarihi.message}</p>
            )}
          </div>
        </div>

        {/* Aktif Durumu */}
        <div>
          <label className="flex items-center space-x-2">
            <input
              type="checkbox"
              {...register('aktif')}
              className="rounded border-gray-300 text-primary-600 focus:ring-primary-500"
            />
            <span className="text-sm font-medium text-gray-700">Aktif Personel</span>
          </label>
        </div>

        {/* Form Butonları */}
        <div className="flex justify-end space-x-3 pt-4">
          <button
            type="button"
            onClick={handleCancel}
            className="btn-secondary"
            disabled={isSubmitting}
          >
            İptal
          </button>
          <button
            type="submit"
            className="btn-primary"
            disabled={isSubmitting}
          >
            {isSubmitting ? 'İşleniyor...' : (personel ? 'Güncelle' : 'Ekle')}
          </button>
        </div>
      </form>
    </div>
  );
};

export default PersonelForm;
