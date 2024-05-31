import { Button, Typography } from '@mui/joy';
import { ContentSection } from '../components/ContentSection.tsx';
import { TeacherTable } from '../components/TeacherTable.tsx';

export function TeacherPage() {
  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between' }}>
        <Typography level="h3">Lehrerübersicht</Typography>
        <Button>Lehrer hinzufügen</Button>
      </div>
      <TeacherTable />
    </ContentSection>
  );
}
