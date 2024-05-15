import { ErrorOutline } from '@mui/icons-material';

import { Button, FormControl, FormHelperText, FormLabel, Input, Snackbar, Typography, styled } from '@mui/joy';
import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useNavigate } from 'react-router-dom';
import { CreateUserDto, UserRessourceService } from '../state/api/generated';

type Inputs = {
  firstname: string;
  middlename?: string;
  lastname: string;
  email: string;
  password: string;
  confirmPassword: string;
};

const EMAIL_REGEX = /^[\w-.]+@([\w-]+\.)+[\w-]{2,}$/;
const PASSWORD_REGEX = /^(?=.*\d)(?=.*[A-Z])(?=.*[a-z])(?=.*[^\w\d\s:])([^\s]){8,}$/;

export function RegisterForm() {
  const {
    register,
    handleSubmit,
    setError,
    formState: { errors },
  } = useForm<Inputs>();
  const [showSnackbarError, setShowSnackbarError] = useState(false);
  const registerMutation = useRegisterMutation(() => setShowSnackbarError(true));
  const onSubmit = (data: Inputs) => {
    const { firstname, middlename, lastname, email, password, confirmPassword } = data;
    if (!EMAIL_REGEX.test(email)) {
      setError('email', { type: 'invalid' });
      return;
    }
    if (!PASSWORD_REGEX.test(password)) {
      setError('password', { type: 'weak' });
      return;
    }
    if (password != confirmPassword) {
      setError('confirmPassword', { type: 'match' });
      return;
    }
    registerMutation.mutate({ firstname, middlename, lastname, email, password });
  };

  return (
    <div style={{ marginTop: '65px', display: 'grid' }}>
      <form onSubmit={handleSubmit(onSubmit)}>
        <div style={{ display: 'grid', justifyContent: 'center', flexWrap: 'wrap' }}>
          <Typography sx={{ color: '#E191A1', fontSize: '35px', fontWeight: '600', width: '750px' }}>
            Registrieren
          </Typography>
          <FormGrid>
            <NameGrid>
              <FormControl error={!!errors.firstname}>
                <StyledFormLabel>Vorname: *</StyledFormLabel>
                <Input {...register('firstname', { required: true })} placeholder={'Max'} />
                {errors.firstname && <FormHelperText>Vorname muss gesetzt sein</FormHelperText>}
              </FormControl>
              <FormControl>
                <StyledFormLabel>2. Vorname: </StyledFormLabel>
                <Input {...register('middlename')} placeholder={'Alexander'} />
              </FormControl>
              <FormControl error={!!errors.lastname}>
                <StyledFormLabel>Nachname: *</StyledFormLabel>
                <Input {...register('lastname', { required: true })} placeholder={'Mustermann'} />
                {errors.lastname && <FormHelperText>Nachname muss gesetzt sein</FormHelperText>}
              </FormControl>
            </NameGrid>
            <FormControl sx={{ height: 90 }} error={!!errors.email}>
              <StyledFormLabel>E-Mail: *</StyledFormLabel>
              <Input {...register('email', { required: true })} placeholder={'max.mustermann@email.com'} />
              <FormHelperText>
                {errors.email?.type == 'required' && 'E-Mail muss gesetzt sein'}
                {errors.email?.type == 'invalid' && 'E-Mail Adresse nicht gültig'}
              </FormHelperText>
            </FormControl>
            <PasswordGrid>
              <FormControl error={!!errors.password}>
                <StyledFormLabel>Passwort: *</StyledFormLabel>
                <Input {...register('password', { required: true })} type={'password'} placeholder={'****'} />
                <FormHelperText>
                  {errors.password?.type == 'required' && 'Passwort muss gesetzt sein'}
                  {errors.password?.type == 'weak' && 'Passwort zu schwach'}
                  {/*@Todo I would an info icon with a tooltip to this error where we can put in the specifications.
                    <div style={{ gridColumn: 'span 2' }}>
                      {errors.password?.type == 'weak' &&
                        !showPasswordNotMatchError &&
                        !errors.password &&
                        !errors.confirmPassword && (
                          <FormHelperText sx={{ color: 'danger' }}>
                            Ihres Passwort muss mindesten 8 charaktere lang sein und muss Gross-/Kleinbuchstaben, Zahlen sowie
                            Sonderzeichen beinhalten
                          </FormHelperText>
                        )}
                    </div>
                  */}
                </FormHelperText>
              </FormControl>
              <FormControl error={!!errors.confirmPassword}>
                <StyledFormLabel>Passwort bestätigen: *</StyledFormLabel>
                <Input {...register('confirmPassword', { required: true })} type={'password'} placeholder={'****'} />
                <FormHelperText>
                  {errors.confirmPassword?.type == 'required' && 'Passwort muss bestätigt sein'}
                  {errors.confirmPassword?.type == 'match' && 'Passwörter stimmen nicht überein'}
                </FormHelperText>
              </FormControl>
            </PasswordGrid>
          </FormGrid>
          <Button loading={registerMutation.isPending} type={'submit'} sx={{ width: '750px' }}>
            Registrieren
          </Button>
        </div>
      </form>
      <Snackbar
        open={showSnackbarError}
        color={'danger'}
        autoHideDuration={3000}
        onClose={() => {
          setShowSnackbarError(false);
        }}
        startDecorator={<ErrorOutline />}>
        Beim speichern Ihrer Registrierung, ist ein Fehler aufgetreten.
      </Snackbar>
    </div>
  );
}

const useRegisterMutation = (onError: () => void) => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: (createUserDto: CreateUserDto) => UserRessourceService.register(createUserDto),
    onSuccess: (response) => {
      localStorage.setItem('token', response.token);
      navigate('/');
    },
    onError,
  });
};

const FormGrid = styled('div')`
  width: 750px;
  display: grid;
  align-items: start;
  margin-bottom: 15px;
`;

const NameGrid = styled('div')`
  display: grid;
  gap: 10px;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  align-items: start;
  height: 90px;
`;
const PasswordGrid = styled('div')`
  display: grid;
  gap: 10px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  align-items: start;
  height: 90px;
`;

const StyledFormLabel = styled(FormLabel)`
  color: ${({ theme }) => theme.palette.primary[500]};
`;
