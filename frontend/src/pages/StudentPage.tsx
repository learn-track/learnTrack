import { ErrorOutline } from '@mui/icons-material';
import { Snackbar, Typography } from '@mui/joy';
import { useState } from 'react';
import { AlternateButton } from '../components/AlternateButton.tsx';
import { ContentSection } from '../components/ContentSection.tsx';
import { CreateStudentModal } from '../components/CreateStudentModal.tsx';
import { StudentTable } from '../components/StudentTable.tsx';

export function StudentPage() {
  const [isOpen, setOpen] = useState(false);

  const [showSnackbar, setShowSnackbar] = useState(false);
  const [showSnackbarError, setShowSnackbarError] = useState(false);
  return (
    <ContentSection>
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
        <Typography level="h3" margin={0} lineHeight={1}>
          Schülerübersicht
        </Typography>
        <AlternateButton onClick={() => setOpen(true)}>Schüler hinzufügen</AlternateButton>
      </div>
      <StudentTable />
      <CreateStudentModal open={isOpen} onClose={() => setOpen(false)} />
      <Snackbar
        open={showSnackbarError}
        color={'danger'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbarError(false);
        }}
        startDecorator={<ErrorOutline />}>
        Beim speichern Ihrer Registrierung ist ein Fehler aufgetreten.
      </Snackbar>
      <Snackbar
        open={showSnackbar}
        color={'success'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbar(false);
        }}>
        Die Klasse wurde erfolgreich erstellt
      </Snackbar>
    </ContentSection>
  );
}
