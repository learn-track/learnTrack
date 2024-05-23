import { atomWithSuspenseQuery } from 'jotai-tanstack-query';
import { WhoamiResourceService } from './generated';

const whoamiAtom = atomWithSuspenseQuery(() => ({
  queryKey: ['whoami'],
  queryFn: () => WhoamiResourceService.getSelf(),
}));

export { whoamiAtom };
