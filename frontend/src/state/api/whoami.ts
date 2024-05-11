import { atomWithSuspenseQuery } from 'jotai-tanstack-query';
import { WhoamiRessourceService } from './generated';

const whoamiAtom = atomWithSuspenseQuery(() => ({
  queryKey: ['whoami'],
  queryFn: () => WhoamiRessourceService.getSelf(),
}));

export { whoamiAtom };
