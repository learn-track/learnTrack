import { ErrorOutline } from '@mui/icons-material';
import { Modal, ModalDialog, Snackbar, Typography } from '@mui/joy';
import { useState } from 'react';
import { StudentRegisterForm } from './StudentRegisterForm.tsx';

export function CreateStudentModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const [showSuccessSnackbar, setShowSuccessSnackbar] = useState(false);
  const [showErrorSnackbar, setShowErrorSnackbar] = useState(false);

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <ModalDialog sx={{ padding: '30px' }}>
          <Typography fontSize={35} marginBottom={2}>
            Neuer Schüler erstellen
          </Typography>
          <StudentRegisterForm
            setShowSuccessSnackbar={setShowSuccessSnackbar}
            setShowErrorSnackbar={setShowErrorSnackbar}
            onClose={onClose}
          />
        </ModalDialog>
      </Modal>
      <Snackbar
        open={showSuccessSnackbar}
        color={'success'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSuccessSnackbar(false);
        }}>
        Klasse wurde erfolgreich erstellt
      </Snackbar>
      <Snackbar
        open={showErrorSnackbar}
        color={'danger'}
        autoHideDuration={3000}
        onClose={() => {
          setShowErrorSnackbar(false);
        }}
        startDecorator={<ErrorOutline />}>
        Ein Fehler ist wärend der Kreierung aufgetreten
      </Snackbar>
    </>
  );
}
