import { styled, Typography } from '@mui/joy';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { StudentTable } from '../components/StudentTable.tsx';

export function GradeDetailPage() {
  return (
    <ContentSection>
      <DetailPageTitle level="h2" margin={0} lineHeight={1}>
        IT 3b
      </DetailPageTitle>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '40px' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Schülerübersicht
        </Typography>
        <AlternateButton type="submit">Schüler hinzufügen</AlternateButton>
      </div>
      <StudentTable></StudentTable>
      <div
        style={{
          display: 'flex',
          justifyContent: 'space-between',
          alignItems: 'center',
          marginBottom: '40px',
          marginTop: '60px',
        }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Fächerübersicht
        </Typography>
        <AlternateButton type="submit">Fach hinzufügen</AlternateButton>
      </div>

      <StudentTable></StudentTable>
    </ContentSection>
  );
}

const DetailPageTitle = styled(Typography)`
  margin-bottom: 60px;
`;
