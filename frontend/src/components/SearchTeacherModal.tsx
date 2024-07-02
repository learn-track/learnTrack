import { ErrorOutline } from '@mui/icons-material';
import { Autocomplete, AutocompleteOption, Button, Modal, ModalDialog, Snackbar, styled, Typography } from '@mui/joy';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useAtom } from 'jotai';
import { useEffect, useState } from 'react';
import { AdminTeacherResourceService } from '../state/api/generated';
import { useSearchTeacherByEmailQuery } from '../state/api/teachers.ts';
import { whoamiAtom } from '../state/api/whoami.ts';

export function SearchTeacherModal({ open, onClose }: { open: boolean; onClose: () => void }) {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const [searchText, setSearchText] = useState('');
  const [isSearchQueryEnabled, setIsSearchQueryEnabled] = useState(false);
  const [selectedTeacherId, setSelectedTeacherId] = useState<null | string>(null);
  const { data, isPending } = useSearchTeacherByEmailQuery(whoami.schools[0].id, searchText, isSearchQueryEnabled);
  const [showErrorSnackbar, setShowErrorSnackBar] = useState(false);
  const [showSuccessSnackbar, setShowSuccessSnackbar] = useState(false);

  const addTeacherToSchoolMutation = useAddTeacherToSchoolMutation(
    whoami.schools[0].id,
    () => {
      setShowSuccessSnackbar(true);
      onClose();
    },
    () => setShowErrorSnackBar(true),
  );

  useEffect(() => {
    if (searchText.length >= 3) {
      setIsSearchQueryEnabled(true);
    } else {
      setIsSearchQueryEnabled(false);
    }
  }, [searchText]);

  return (
    <>
      <Modal open={open} onClose={onClose}>
        <ModalDialog sx={{ minWidth: 450 }}>
          <Typography level={'h4'}>Lehrer hinzufügen</Typography>
          <Autocomplete
            options={data ?? []}
            placeholder={'Nach E-Mail suchen'}
            inputValue={searchText}
            onInputChange={(_, value) => setSearchText(value)}
            onChange={(_, value) => setSelectedTeacherId(value && value.id)}
            getOptionLabel={(option) => `${option.email} `}
            noOptionsText={
              isSearchQueryEnabled
                ? isPending
                  ? 'Lädt...'
                  : 'Keine ergebnisse'
                : 'Bitte geben Sie mindestens drei Zeichen ein, um die Suche zu starten.'
            }
            renderOption={(props, option) => (
              <AutocompleteOption {...props} key={option.id} aria-disabled={option.isAssignedToSchool}>
                {option.email}
              </AutocompleteOption>
            )}
          />
          <ModalButtonsWrapper>
            <Button onClick={onClose} variant={'plain'}>
              Abbrechen
            </Button>
            <Button
              disabled={!selectedTeacherId}
              onClick={() => {
                if (selectedTeacherId) {
                  addTeacherToSchoolMutation.mutate(selectedTeacherId);
                }
              }}>
              Speichern
            </Button>
          </ModalButtonsWrapper>
        </ModalDialog>
      </Modal>
      <Snackbar
        open={showSuccessSnackbar}
        color={'success'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSuccessSnackbar(false);
        }}>
        Lehrer wurde erfolgreich zur Schule hinzugefügt.
      </Snackbar>
      <Snackbar
        open={showErrorSnackbar}
        color={'danger'}
        autoHideDuration={3000}
        onClose={() => {
          setShowErrorSnackBar(false);
        }}
        startDecorator={<ErrorOutline />}>
        Beim hinzufügen des Lehrers ist ein Fehler aufgetreten.
      </Snackbar>
    </>
  );
}

const useAddTeacherToSchoolMutation = (schoolId: string, onSuccess: () => void, onError: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (teacherId: string) => AdminTeacherResourceService.assignTeacherToSchool(schoolId, teacherId),
    onSuccess: () => {
      void queryClient.invalidateQueries({ queryKey: ['teacherList', schoolId] });
      onSuccess();
    },
    onError,
  });
};

const ModalButtonsWrapper = styled('div')`
  margin-top: 1.5rem;
  display: flex;
  gap: 24px;
  justify-content: space-between;
`;
