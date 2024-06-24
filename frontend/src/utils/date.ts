import { format } from 'date-fns';

export const formatDate = (date: Date | undefined) => date && format(date, 'dd.MM.yyyy');
