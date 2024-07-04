import { styled, Typography } from '@mui/joy';
import { useAtom } from 'jotai/index';
import { useState } from 'react';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { CreateGradeModal } from '../components/CreateGradeModal.tsx';
import { GradeCard } from '../components/GradeCard.tsx';
import { useGetGradesForSchoolQuery } from '../state/api/grades.ts';
import { whoamiAtom } from '../state/api/whoami.ts';

export function GradePage() {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const schoolId = whoami.schools[0].id;
  const [isCreateGradeModalOpen, setIsCreateGradeModalOpen] = useState(false);

  const grades = useGetGradesForSchoolQuery(schoolId);

  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '40px' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Klassenübersicht
        </Typography>
        <AlternateButton onClick={() => setIsCreateGradeModalOpen(true)}>Klasse hinzufügen</AlternateButton>
        <CreateGradeModal
          open={isCreateGradeModalOpen}
          onClose={() => setIsCreateGradeModalOpen(false)}></CreateGradeModal>
      </div>
      <GradesCardWrapper>
        {grades?.map((gradeDetailsDto) => (
          <GradeCard
            key={gradeDetailsDto.grade.id}
            className={gradeDetailsDto.grade.name}
            teacherCount={gradeDetailsDto.teachers.length}
            studentCount={gradeDetailsDto.students.length}
          />
        ))}
      </GradesCardWrapper>
    </ContentSection>
  );
}

const GradesCardWrapper = styled('div')`
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  justify-content: center;
  min-width: 100%;
  gap: 60px;
`;
