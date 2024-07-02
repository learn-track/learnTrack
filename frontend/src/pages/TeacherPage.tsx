import { Typography } from '@mui/joy';
import { useState } from 'react';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { SearchTeacherModal } from '../components/SearchTeacherModal.tsx';
import { TeacherTable } from '../components/TeacherTable.tsx';

export function TeacherPage() {
  const [isSearchTeacherModalOpen, setIsSearchTeacherModalOpen] = useState(false);
  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Lehrerübersicht
        </Typography>
        <AlternateButton onClick={() => setIsSearchTeacherModalOpen(true)}>Lehrer hinzufügen</AlternateButton>
        <SearchTeacherModal open={isSearchTeacherModalOpen} onClose={() => setIsSearchTeacherModalOpen(false)} />
      </div>
      <TeacherTable />
    </ContentSection>
  );
}
