import {
  Autocomplete,
  AutocompleteOption,
  FormControl,
  FormHelperText,
  FormLabel,
  Input,
  styled,
  Tooltip,
} from '@mui/joy';
import { useMutation, useQueryClient } from '@tanstack/react-query';
import { useAtom } from 'jotai/index';
import { Controller, useForm } from 'react-hook-form';
import { AdminStudentResourceService, CreateStudentDto, GradeDetailsDto } from '../state/api/generated';
import { useGetGradesForSchoolQuery } from '../state/api/grades.ts';
import { whoamiAtom } from '../state/api/whoami.ts';
import { AlternateButton } from './AlternateButton.tsx';

type Inputs = {
  firstname: string;
  middlename?: string;
  lastname: string;
  email: string;
  birthDate: string;
  password: string;
  gradeDetails: GradeDetailsDto;
};

const EMAIL_REGEX = /^[\w-.]+@([\w-]+\.)+[\w-]{2,}$/;
const PASSWORD_REGEX = /^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,}$/;
export function StudentRegisterForm({
  setShowSuccessSnackbar,
  setShowErrorSnackbar,
  onClose,
}: {
  setShowSuccessSnackbar: (open: boolean) => void;
  setShowErrorSnackbar: (open: boolean) => void;
  onClose: () => void;
}) {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const grades = useGetGradesForSchoolQuery(whoami.schools[0].id);

  const {
    control,
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<Inputs>();
  const registerMutation = useStudentRegisterMutation(
    whoami.schools[0].id,
    () => {
      onClose();
      setShowSuccessSnackbar(true);
    },
    () => setShowErrorSnackbar(true),
  );
  const onSubmit = (data: Inputs) => {
    if (!EMAIL_REGEX.test(data.email)) {
      setError('email', { type: 'invalid' });
      return;
    }
    if (!PASSWORD_REGEX.test(data.password)) {
      setError('password', { type: 'weak' });
      return;
    }
    const date = new Date(data.birthDate);
    const formattedDate = date.toISOString();
    const requestData: CreateStudentDto = {
      firstname: data.firstname,
      middlename: data.middlename,
      lastname: data.lastname,
      email: data.email,
      birthDate: formattedDate,
      password: data.password,
      schoolId: whoami.schools[0].id,
      gradeId: data.gradeDetails.grade.id,
    };

    registerMutation.mutate(requestData);
  };

  return (
    <>
      <StudentRegisterFormWrapper onSubmit={handleSubmit(onSubmit)}>
        <ThreeColGrid>
          <FixedHeightFormControl error={!!errors.firstname}>
            <StyledFormLabel>Vorname: *</StyledFormLabel>
            <Input {...register('firstname', { required: true })} placeholder={'Max'} />
            {errors.firstname && <FormHelperText>Vorname muss gesetzt sein</FormHelperText>}
          </FixedHeightFormControl>
          <FixedHeightFormControl>
            <StyledFormLabel>2. Vorname: </StyledFormLabel>
            <Input {...register('middlename')} placeholder={'Alexander'} />
          </FixedHeightFormControl>
          <FixedHeightFormControl error={!!errors.lastname}>
            <StyledFormLabel>Nachname: *</StyledFormLabel>
            <Input {...register('lastname', { required: true })} placeholder={'Mustermann'} />
            {errors.lastname && <FormHelperText>Nachname muss gesetzt sein</FormHelperText>}
          </FixedHeightFormControl>
        </ThreeColGrid>
        <TwoColGrid>
          <FixedHeightFormControl error={!!errors.email}>
            <StyledFormLabel>E-Mail: *</StyledFormLabel>
            <Input {...register('email', { required: true })} placeholder={'max.mustermann@email.com'} />
            <FormHelperText>
              {errors.email?.type == 'required' && 'E-Mail muss gesetzt sein'}
              {errors.email?.type == 'invalid' && 'E-Mail Adresse nicht gültig'}
            </FormHelperText>
          </FixedHeightFormControl>
          <FixedHeightFormControl error={!!errors.birthDate}>
            <StyledFormLabel>Geburtsdatum: *</StyledFormLabel>
            <Input {...register('birthDate', { required: true })} type="date" />
            <FormHelperText>
              {errors.birthDate?.type == 'required' && 'Geburtsdatum muss gesetzt sein'}
              {errors.birthDate?.type == 'invalid' && 'Geburtsdatum nicht gültig'}
            </FormHelperText>
          </FixedHeightFormControl>
        </TwoColGrid>

        <FixedHeightFormControl error={!!errors.password}>
          <StyledFormLabel>Passwort: *</StyledFormLabel>
          <Input {...register('password', { required: true })} type={'password'} placeholder={'********'} />
          {errors.password?.type == 'required' && <FormHelperText>Passwort muss gesetzt sein</FormHelperText>}
          {errors.password?.type == 'weak' && (
            <Tooltip
              placement={'bottom-start'}
              color="danger"
              title="Ihres Passwort muss mindesten 8 charaktere lang sein und muss Gross-/Kleinbuchstaben, Zahlen sowie
                            Sonderzeichen beinhalten">
              <FormHelperText>Passwort zu schwach</FormHelperText>
            </Tooltip>
          )}
        </FixedHeightFormControl>
        <FixedHeightFormControl error={!!errors.gradeDetails}>
          <StyledFormLabel>Klasse auswählen: *</StyledFormLabel>
          {grades && (
            <Controller
              name="gradeDetails"
              control={control}
              rules={{ required: true }}
              render={({ field: { onChange } }) => (
                <Autocomplete
                  onChange={(_, value) => onChange(value)}
                  options={grades ?? []}
                  getOptionLabel={(option) => option.grade.name}
                  renderOption={(props, option) => (
                    <AutocompleteOption {...props} key={option.grade.id}>
                      {option.grade.name}
                    </AutocompleteOption>
                  )}
                />
              )}
            />
          )}
          {errors.gradeDetails?.type === 'required' && <FormHelperText>Keine Klasse gewählt</FormHelperText>}
        </FixedHeightFormControl>

        <AlternateButton loading={registerMutation.isPending} type={'submit'} sx={{ width: '750px' }}>
          Schüler erstellen
        </AlternateButton>
      </StudentRegisterFormWrapper>
    </>
  );
}

const useStudentRegisterMutation = (schoolId: string, onSuccess: () => void, onError: () => void) => {
  const queryClient = useQueryClient();
  return useMutation({
    mutationFn: (createStudentDto: CreateStudentDto) =>
      AdminStudentResourceService.createStudent(schoolId, createStudentDto),
    onSuccess: () => {
      onSuccess();

      void queryClient.invalidateQueries({ queryKey: ['studentList', schoolId] });
    },
    onError,
  });
};

const StudentRegisterFormWrapper = styled('form')`
  min-height: 340px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
`;
const ThreeColGrid = styled('div')`
  display: grid;
  gap: 15px;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  align-items: start;
  height: 90px;
`;
const TwoColGrid = styled('div')`
  display: grid;
  gap: 15px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  align-items: start;
  height: 90px;
`;

const FixedHeightFormControl = styled(FormControl)`
  min-height: 100px;
`;

const StyledFormLabel = styled(FormLabel)`
  color: ${({ theme }) => theme.palette.primary[500]};
`;
