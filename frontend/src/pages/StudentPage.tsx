import { Typography } from '@mui/joy';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { StudentTable } from '../components/StudentTable.tsx';

export function StudentPage() {
  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Schülerübersicht
        </Typography>
        <AlternateButton>Schüler hinzufügen</AlternateButton>
      </div>
      <StudentTable />
    </ContentSection>
  );
}
