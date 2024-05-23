import { useQuery } from '@tanstack/react-query';
import { UserResourceService } from './generated';

export const useUserQuery = () => {
  const { data } = useQuery({
    queryKey: ['whoami'],
    queryFn: () => UserResourceService.test(),
  });

  return data;
};
