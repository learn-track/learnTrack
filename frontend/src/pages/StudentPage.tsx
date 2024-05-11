import { useUserQuery } from '../state/api/user.ts';

export function StudentPage() {
  const backendDto = useUserQuery();

  return (
    <>
      <h1>Schüler 📚🥳</h1>
      <div>{backendDto}</div>
    </>
  );
}
