import { Typography } from '@mui/joy';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { TeacherTable } from '../components/TeacherTable.tsx';

export function TeacherPage() {
  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Lehrerübersicht
        </Typography>
        <AlternateButton>Lehrer hinzufügen</AlternateButton>
      </div>
      <TeacherTable />
    </ContentSection>
  );
}
