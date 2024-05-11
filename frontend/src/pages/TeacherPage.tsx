import { useUserQuery } from '../state/api/user.ts';

export function TeacherPage() {
  const backendDto = useUserQuery();

  return (
    <>
      <h1>Lehrer 📚🥳</h1>
      <div>{backendDto}</div>
    </>
  );
}
