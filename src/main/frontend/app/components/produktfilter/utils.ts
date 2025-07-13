import { FilterTyp } from '@/main/frontend/app/model/FilterTyp';

export function erstelleFilterTabId(filterTyp: FilterTyp): `filter-tab-${FilterTyp}` {
  return `filter-tab-${filterTyp}`;
}

export function erstelleFilterTabPanelId(filterTyp: FilterTyp): `filter-tabpanel-${FilterTyp}` {
  return `filter-tabpanel-${filterTyp}`;
}
