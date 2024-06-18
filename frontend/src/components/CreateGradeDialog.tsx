import { ErrorOutline } from '@mui/icons-material';
import { Button, Input, Modal, ModalDialog, Snackbar, Typography } from '@mui/joy';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useAtom } from 'jotai/index';
import { useState } from 'react';
import { AdminGradeResourceService, CreateGradeDto } from '../state/api/generated';
import { whoamiAtom } from '../state/api/whoami.ts';

export function CreateGradeDialog({ isOpen, setOpen }: { isOpen: boolean; setOpen: (open: boolean) => void }) {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const schoolId = whoami.schools[0].id;
  const [gradeName, setGradeName] = useState('');

  const [showSnackbar, setShowSnackbar] = useState(false);
  const [showSnackbarError, setShowSnackbarError] = useState(false);

  const createGrade = useCreateGradeForSchoolMutation(
    schoolId,
    () => {
      setShowSnackbar(true);
    },
    () => setShowSnackbarError(true),
  );

  const handleModelSubmit = () => {
    if (gradeName != '') {
      createGrade.mutate({ name: gradeName, schoolId });
    } else {
      setShowSnackbarError(true);
    }
    setOpen(false);
  };

  return (
    <>
      <Modal open={isOpen} onClose={() => setOpen(false)}>
        <ModalDialog>
          <Typography level="h4">Neue Klasse erstellen</Typography>
          <Input
            value={gradeName}
            onChange={(e) => setGradeName(e.target.value)}
            placeholder="Geben sie den namen ein"
            fullWidth
          />
          <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
            <Button onClick={() => setOpen(false)}>Cancel</Button>
            <Button onClick={handleModelSubmit}>Submit</Button>
          </div>
        </ModalDialog>
      </Modal>
      <Snackbar
        open={showSnackbar}
        color={'success'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbar(false);
        }}>
        Die Klasse wurde erfolgreich erstellt
      </Snackbar>
      <Snackbar
        open={showSnackbarError}
        color={'danger'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbarError(false);
        }}
        startDecorator={<ErrorOutline />}>
        Beim erstellend der Klasse ist ein Fehler aufgetreten.
      </Snackbar>
    </>
  );
}

const useCreateGradeForSchoolMutation = (schoolId: string, onSuccess: () => void, onError: () => void) => {
  const queryClient = useQueryClient();

  return useMutation({
    mutationFn: (requestBody: CreateGradeDto) => AdminGradeResourceService.createGrade(schoolId, requestBody),
    onSuccess: () => {
      void queryClient.invalidateQueries({ queryKey: ['gradeList', schoolId] });
      onSuccess();
    },
    onError,
  });
};
