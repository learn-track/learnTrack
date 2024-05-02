import { ErrorOutline } from '@mui/icons-material';

import { Button, FormControl, FormHelperText, FormLabel, Input, Snackbar, Typography, styled } from '@mui/joy';
import { useState } from 'react';
import { useForm } from 'react-hook-form';
import { useMutation } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { UserRessourceService } from '../state/api/generated';

type Inputs = {
  firstname: string;
  middlename?: string;
  lastname: string;
  email: string;
  password: string;
  confirmPassword: string;
};

export function RegisterForm() {
  //const navigate = useNavigate();
  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<Inputs>();
  const [showPasswordNotMatchError, setShowPasswordNotMatchError] = useState(false);
  const [showStrongPasswordError, setShowStrongPasswordError] = useState(false);
  const [showInvalidEmailError, setShowInvalidEmailError] = useState(false);
  const [showSnackbarError, setShowSnackbarError] = useState(false);
  const registerMutation = useRegisterMutation();

  const onSubmit = (data: Inputs) => {
    const { firstname, middlename, lastname, email, password, confirmPassword } = data;

    if (CheckEmailValid(email)) return;
    if (CheckPasswordMatch(password, confirmPassword)) return;
    if (CheckPasswordStrong(password)) return;

    registerMutation.mutate({ firstname, middlename, lastname, email, password });
  };

  const CheckPasswordMatch = (firstPassword: string, secondPassword: string) => {
    if (firstPassword !== secondPassword) {
      setShowPasswordNotMatchError(true);
      setShowSnackbarError(true);
      return true;
    }
    setShowPasswordNotMatchError(false);
    return false;
  };

  const CheckPasswordStrong = (password: string) => {
    if (!validatePassword(password)) {
      setShowStrongPasswordError(true);
      setShowSnackbarError(true);
      return true;
    }
    setShowStrongPasswordError(false);
    return false;
  };

  const validatePassword = (password: string) => {
    return password.match(/^(?=.*[0-9])(?=.*[!@#$%^&*])[a-zA-Z0-9!@#$%^&*]{8,}$/);
  };

  const CheckEmailValid = (email: string) => {
    if (!validateEmail(email)) {
      setShowInvalidEmailError(true);
      setShowSnackbarError(true);
      return true;
    }
    setShowInvalidEmailError(false);
    return false;
  };

  const validateEmail = (email: string) => {
    return email.match(
      /^(([^<>()[\]\\.,;:\s@\"]+(\.[^<>()[\]\\.,;:\s@\"]+)*)|(\".+\"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/,
    );
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
                <div style={{ height: errors.firstname ? '0' : '26px' }}></div>
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
            <FormControl error={!!errors.email || showInvalidEmailError}>
              <StyledFormLabel>E-Mail: *</StyledFormLabel>
              <Input {...register('email', { required: true })} placeholder={'max.mustermann@email.com'} />
              {errors.email && <FormHelperText>E-Mail muss gesetzt sein</FormHelperText>}
              {showInvalidEmailError && !errors.email && (
                <FormHelperText>Verwenden Sie eine gültige E-Mail-Adresse</FormHelperText>
              )}
              <div style={{ height: errors.email || showInvalidEmailError ? '0' : '26px' }}></div>
            </FormControl>
            <PasswordGrid>
              <FormControl error={!!errors.password || showPasswordNotMatchError || showStrongPasswordError}>
                <StyledFormLabel>Passwort: *</StyledFormLabel>
                <Input {...register('password', { required: true })} type={'password'} placeholder={'****'} />
                {errors.password && <FormHelperText color={'danger'}>Passwort muss gesetzt sein</FormHelperText>}
                {showPasswordNotMatchError && !errors.password && (
                  <FormHelperText>Passwörter stimmen nicht überein</FormHelperText>
                )}
              </FormControl>
              <FormControl error={!!errors.confirmPassword || showPasswordNotMatchError}>
                <StyledFormLabel>Passwort bestätigen: *</StyledFormLabel>
                <Input {...register('confirmPassword', { required: true })} type={'password'} placeholder={'****'} />
                {errors.confirmPassword && <FormHelperText>Passwort muss bestätigt sein</FormHelperText>}
              </FormControl>
              <div style={{ gridColumn: 'span 2' }}>
                {showStrongPasswordError &&
                  !showPasswordNotMatchError &&
                  !errors.password &&
                  !errors.confirmPassword && (
                    <FormHelperText sx={{ color: '#C41C1C' }}>
                      Ihres Passwort muss mindesten 8 charaktere lang sein und muss Gross-/Kleinbuchstaben, Zahlen sowie
                      Sonderzeichen beinhalten
                    </FormHelperText>
                  )}
              </div>
              <div
                style={{
                  height: errors.password || showPasswordNotMatchError || showStrongPasswordError ? '0' : '26px',
                }}></div>
            </PasswordGrid>
          </FormGrid>
          <Button type={'submit'} sx={{ width: '750px' }}>
            Registrieren
          </Button>
        </div>
      </form>
      <Snackbar
        open={showSnackbarError}
        color={'danger'}
        onClose={(clickaway) => {
          if (clickaway) {
            setShowSnackbarError(false);
          }
        }}
        startDecorator={<ErrorOutline />}>
        {'Fehler bei der Registrierung'}
      </Snackbar>
    </div>
  );
}

const useRegisterMutation = () => {
  const navigate = useNavigate();

  return useMutation({
    mutationFn: async (data: Inputs) => {
      const { firstname, middlename, lastname, email, password } = data;
      try {
        const response = await UserRessourceService.register({ firstname, middlename, lastname, email, password });
        localStorage.setItem('token', response.token);
        navigate('/');
        return response; // Return the response data from the mutation
      } catch (error) {
        throw error;
      }
    },
  });
};

const FormGrid = styled('div')`
  width: 750px;
  display: grid;
  align-items: start;
  margin-bottom: 0;
`;

const NameGrid = styled('div')`
  display: grid;
  gap: 10px;
  grid-template-columns: repeat(3, minmax(0, 1fr));
  align-items: start;
`;
const PasswordGrid = styled('div')`
  display: grid;
  gap: 10px;
  grid-template-columns: repeat(2, minmax(0, 1fr));
  align-items: start;
`;

const StyledFormLabel = styled(FormLabel)`
  color: ${({ theme }) => theme.palette.primary[500]};
`;
