import { useUserQuery } from '../state/api/user.ts';

export function LandingPage() {
  const backendDto = useUserQuery();

  return (
    <>
      <h1>learnUp 📚🥳</h1>
      <div>{backendDto}</div>
    </>
  );
}
