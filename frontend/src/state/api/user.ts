import { useQuery } from '@tanstack/react-query';
import { UserRessourceService } from './generated';

export const useUserQuery = () => {
  const { data } = useQuery({
    queryKey: ['whoami'],
    queryFn: () => UserRessourceService.test(),
  });

  return data;
};
