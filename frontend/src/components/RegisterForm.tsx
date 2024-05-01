import {ErrorOutline} from '@mui/icons-material';

import {Button, FormControl, Grid, Input, Snackbar, Typography} from '@mui/joy';
import { useState } from 'react';
import { useMutation } from 'react-query';
import { useNavigate } from 'react-router-dom';
import { UserRessourceService } from '../state/api/generated';


export function RegisterForm() {
    const [showInputError, setShowInputError] = useState(false);
    const [showSnackbarError, setShowSnackbarError] = useState(false);
    const [firstname, setFirstname] = useState('');
    const [middlename, setMiddlename] = useState('');
    const [lastname, setLastname] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');
    const [errorMessage, setErrorMessage] = useState('Registration fehlgeschlagen');//ich will den error message zurückbekommen, weis aber noch nicht genau wie oder ob das geht
    const registerMutation = useRegisterMutation(firstname, middlename, lastname, email, password, confirmPassword, (returnErrorMessage) => {
      setErrorMessage(returnErrorMessage);
      setShowInputError(true);
      setShowSnackbarError(true);
    });
    const inputBackgroundColor = showInputError ? '#fadada' : 'rgba(137, 218, 213, 0.5)';

  return (
    <Grid xs={12} sx={{ marginTop: '25vh' }}>
      <form
          onSubmit={(event) => {
          event.preventDefault();
          registerMutation.mutate();}}
      >
        <Grid sx={{ display: 'flex', justifyContent: 'center', flexWrap: 'wrap' }}>
            <Typography sx={{ color: '#E191A1', fontSize: '35px', fontWeight: '600', width: '50vw'}}>Registrieren</Typography>
            <FormControl error={showInputError}>
                <Input
                    value={firstname}
                    onChange={(e) => setFirstname(e.target.value)}
                    placeholder={'Vorname*'}
                    sx={{
                        background: inputBackgroundColor,
                        width: '16vw',
                        marginRight: '1vw',
                        marginBottom: '3vh',
                        padding: '12px 20px',
                        fontFamily: 'Montserrat, sans-serif',
                        fontSize: '15px',
                    }}
                    variant="plain"
                    size={'lg'}
                />
            </FormControl>
            <FormControl error={showInputError}>
                <Input
                    value={middlename}
                    onChange={(e) => setMiddlename(e.target.value)}
                    placeholder={'2. Vorname'}
                    sx={{
                        background: inputBackgroundColor,
                        width: '16vw',
                        marginRight: '1vw',
                        marginBottom: '3vh',
                        padding: '12px 20px',
                        fontFamily: 'Montserrat, sans-serif',
                        fontSize: '15px',
                    }}
                    variant="plain"
                    size={'lg'}
                />
            </FormControl>
            <FormControl error={showInputError}>
                <Input
                    value={lastname}
                    onChange={(e) => setLastname(e.target.value)}
                    placeholder={'Nachname*'}
                    sx={{
                        background: inputBackgroundColor,
                        width: '16vw',
                        marginBottom: '3vh',
                        padding: '12px 20px',
                        fontFamily: 'Montserrat, sans-serif',
                        fontSize: '15px',
                    }}
                    variant="plain"
                    size={'lg'}
                />
            </FormControl>
          <FormControl error={showInputError}>
            <Input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              placeholder={'E-Mail*'}
              sx={{
                background: inputBackgroundColor,
                  width: '50vw',
                  marginBottom: '3vh',
                padding: '12px 20px',
                fontFamily: 'Montserrat, sans-serif',
                fontSize: '15px',
              }}
              variant="plain"
              size={'lg'}
            />
          </FormControl>
          <FormControl error={showInputError}>
            <Input
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              type={'password'}
              placeholder={'Passwort*'}
              sx={{
                background: inputBackgroundColor,
                  width: '24.5vw',
                  marginRight: '1vw',
                padding: '12px 20px',
                fontFamily: 'Montserrat, sans-serif',
                fontSize: '15px',
              }}
              variant="plain"
              size={'lg'}
            />
              <Typography sx={{marginBottom: '3vh'}}>*Pflichtfelder</Typography>
          </FormControl>
            <FormControl error={showInputError}>
                <Input
                    value={confirmPassword}
                    onChange={(e) => setConfirmPassword(e.target.value)}
                    type={'password'}
                    placeholder={'Passwort bestätigen*'}
                    sx={{
                        background: inputBackgroundColor,
                        width: '24.5vw',
                        padding: '12px 20px',
                        fontFamily: 'Montserrat, sans-serif',
                        fontSize: '15px',
                    }}
                    variant="plain"
                    size={'lg'}
                />
            </FormControl>
          <Button type={'submit'} sx={{width: '50vw'}}>Registrieren</Button>
        </Grid>
      </form>
      <Snackbar
        open={showSnackbarError}
        color={'danger'}
        sx={{ backgroundColor: '#f6e0e0' }}
        onClose={(clickaway) => {
          if (clickaway) {
            setShowSnackbarError(false);
          }
        }}
        startDecorator={<ErrorOutline />}>
          {errorMessage}
      </Snackbar>
    </Grid>
  );
}

const useRegisterMutation = (firstname: string, middlename: string, lastname: string, email: string, password: string, confirmPassword: string, onError: (returnErrorMessage: string) => void) => {
  const navigate = useNavigate();

  return password == confirmPassword ? useMutation({
      mutationFn: () => UserRessourceService.register({ firstname, middlename, lastname, email, password }),
      onSuccess: (data) => {
          localStorage.setItem('token', data.token);
          navigate('/');
      },
      onError: () => onError("Registration fehlgeschlaggen")
  }) : useMutation({
      onError: () => onError("Passwörter stimmen nicht überein")
  });
};
