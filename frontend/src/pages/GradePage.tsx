import { useUserQuery } from '../state/api/user.ts';

export function GradePage() {
  const backendDto = useUserQuery();

  return (
    <>
      <h1>Klasse 📚🥳</h1>
      <div>{backendDto}</div>
    </>
  );
}
