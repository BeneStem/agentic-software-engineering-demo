import { computed, ComputedRef } from 'vue';
import { addDays, format, isPast, isSameDay, isTomorrow } from 'date-fns';
import deLocale from 'date-fns/locale/de';
import { ProduktVerfuegbarkeit } from '@/main/frontend/app/model/ProduktVerfuegbarkeit';

interface ProduktLieferaussage {
  lieferaussage: ComputedRef<string>;
}

export function useProduktLieferaussage(naechstmoeglicheVerfuegbarkeit: ProduktVerfuegbarkeit): ProduktLieferaussage {
  const lieferaussage = computed(() => {
    const liefertag = naechstmoeglicheVerfuegbarkeit.liefertag;

    if (isPast(naechstmoeglicheVerfuegbarkeit.bestellschluss) || isPast(liefertag)) {
      return '';
    } else if (isTomorrow(liefertag)) {
      return 'Heute bestellt - morgen geliefert';
    } else if (isSameDay(liefertag, addDays(Date.now(), 2))) {
      return 'Heute bestellt - übermorgen geliefert';
    } else {
      return `Heute bestellt - ab ${format(liefertag, 'eeeeee, dd.MM.', { locale: deLocale })} geliefert`;
    }
  });

  return {
    lieferaussage,
  };
}
