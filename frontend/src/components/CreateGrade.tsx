import { Button, Input, Modal, ModalDialog, Typography } from '@mui/joy';
import { useMutation } from '@tanstack/react-query';
import { useAtom } from 'jotai/index';
import { useState } from 'react';
import { AdminGradeResourceService, CreateGradeDto } from '../state/api/generated';
import { whoamiAtom } from '../state/api/whoami.ts';

export function CreateGrade({
  isOpen,
  setOpen,
  setShowSnackbar,
  setShowSnackbarError,
}: {
  isOpen: boolean;
  setOpen: (open: boolean) => void;
  setShowSnackbar: (open: boolean) => void;
  setShowSnackbarError: (open: boolean) => void;
}) {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const schoolId = whoami.schools[0].id;
  const [gradeName, setGradeName] = useState('');

  const handleModalClose = () => setOpen(false);
  const createGrade = useCreateGradeForSchool(
    schoolId,
    () => setShowSnackbar(true),
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
    <Modal open={isOpen} onClose={handleModalClose}>
      <ModalDialog>
        <Typography level="h4">Neue Klasse erstellen</Typography>
        <Input
          value={gradeName}
          onChange={(e) => setGradeName(e.target.value)}
          placeholder="Geben sie den namen ein"
          fullWidth
        />
        <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '8px' }}>
          <Button onClick={handleModalClose}>Cancel</Button>
          <Button onClick={handleModelSubmit}>Submit</Button>
        </div>
      </ModalDialog>
    </Modal>
  );
}

const useCreateGradeForSchool = (schoolId: string, onSuccess: () => void, onError: () => void) => {
  return useMutation({
    mutationFn: (requestBody: CreateGradeDto) => AdminGradeResourceService.createGrade(schoolId, requestBody),
    onSuccess,
    onError,
  });
};
