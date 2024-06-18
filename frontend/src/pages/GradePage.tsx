import { ErrorOutline } from '@mui/icons-material';
import { Snackbar, styled, Typography } from '@mui/joy';
import { useQueryClient } from '@tanstack/react-query';
import { useAtom } from 'jotai/index';
import { useEffect, useState } from 'react';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { CreateGrade } from '../components/CreateGrade.tsx';
import { GradeCard } from '../components/GradeCard.tsx';
import { useGetGradesForSchoolQuery } from '../state/api/grades.ts';
import { whoamiAtom } from '../state/api/whoami.ts';

export function GradePage() {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const schoolId = whoami.schools[0].id;
  const [open, setOpen] = useState(false);

  const grades = useGetGradesForSchoolQuery(schoolId);
  const queryClient = useQueryClient();

  const [showSnackbar, setShowSnackbar] = useState(false);
  const [showSnackbarError, setShowSnackbarError] = useState(false);

  useEffect(() => {
    if (showSnackbar == true) {
      queryClient.invalidateQueries(['gradeList', schoolId]);
    }
  }, [showSnackbar]);

  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '40px' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Klassenübersicht
        </Typography>
        <AlternateButton type="submit" onClick={() => setOpen(!open)}>
          Klasse hinzufügen
        </AlternateButton>
        <CreateGrade
          isOpen={open}
          setOpen={setOpen}
          setShowSnackbar={setShowSnackbar}
          setShowSnackbarError={setShowSnackbarError}></CreateGrade>
      </div>
      <GradesCardWrapper>
        {grades?.map((gradeDetailsDto) => {
          return (
            <GradeCard
              key={gradeDetailsDto.grades.name}
              className={gradeDetailsDto.grades.name}
              teacherCount={gradeDetailsDto.teachers.length}
              studentCount={gradeDetailsDto.students.length}
            />
          );
        })}
      </GradesCardWrapper>
      <Snackbar
        open={showSnackbar}
        color={'success'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbar(false);
        }}>
        Klasse wurde erfolgreich erstellt
      </Snackbar>
      <Snackbar
        open={showSnackbarError}
        color={'danger'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbarError(false);
        }}
        startDecorator={<ErrorOutline />}>
        Ein Fehler ist wärend der Kreierung aufgetreten
      </Snackbar>
    </ContentSection>
  );
}

const GradesCardWrapper = styled('div')`
  display: grid;
  grid-template-columns: repeat(3, auto);
  align-items: center;
  justify-items: center;
  gap: 30px;
  width: 100%;
`;
