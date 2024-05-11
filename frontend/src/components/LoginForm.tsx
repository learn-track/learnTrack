import { ErrorOutline, Lock, Mail } from '@mui/icons-material';

import { Button, FormControl, Input, Link, Snackbar, Stack, styled } from '@mui/joy';
import { useMutation } from '@tanstack/react-query';
import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { UserRessourceService } from '../state/api/generated';

export function LoginForm() {
  const [showInputError, setShowInputError] = useState(false);
  const [showSnackbarError, setShowSnackbarError] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const loginMutation = useLoginMutation(email, password, () => {
    setShowInputError(true);
    setShowSnackbarError(true);
  });
  const inputBackgroundColor = showInputError ? '#fadada' : 'rgba(137, 218, 213, 0.5)';
  const inputBorderColor = showInputError ? '2px solid #fc0303' : '2px solid #5EC2B7';

  return (
    <>
      <form
        onSubmit={(event) => {
          event.preventDefault();
          loginMutation.mutate();
        }}>
        <Stack spacing={3} width={350}>
          <FormControl error={showInputError}>
            <Input
              value={email}
              onChange={(e) => setEmail(e.target.value)}
              startDecorator={
                <Mail
                  sx={{
                    fontSize: '25px',
                    color: '#333',
                    borderRight: inputBorderColor,
                    width: '50px',
                    height: '20px',
                  }}
                />
              }
              placeholder={'E-Mail'}
              sx={{
                background: inputBackgroundColor,
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
              startDecorator={
                <Lock
                  sx={{
                    fontSize: '25px',
                    color: '#333',
                    borderRight: inputBorderColor,
                    width: '50px',
                    height: '20px',
                  }}
                />
              }
              placeholder={'Passwort'}
              sx={{
                background: inputBackgroundColor,
                padding: '12px 20px',
                fontFamily: 'Montserrat, sans-serif',
                fontSize: '15px',
              }}
              variant="plain"
              size={'lg'}
            />
          </FormControl>
          <LinkContainer>
            <Link
              sx={{
                marginBottom: '20px',
                '&::before': {
                  backgroundColor: '#5EC2B7',
                },
              }}>
              Passwort vergessen
            </Link>
          </LinkContainer>
          <SubmitButton type={'submit'}>Anmelden</SubmitButton>
        </Stack>
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
        E-Mail oder Passwort ist falsch!
      </Snackbar>
    </>
  );
}

const useLoginMutation = (email: string, password: string, onError: () => void) => {
  const navigate = useNavigate();
  return useMutation({
    mutationFn: () => UserRessourceService.login({ email, password }),
    onSuccess: (data) => {
      localStorage.setItem('token', data.token);
      navigate('/');
    },
    onError: () => onError(),
  });
};

const LinkContainer = styled('div')`
  width: 75%;
`;

const SubmitButton = styled(Button)`
  &:hover {
    background-color: #e191a1;
    &::before {
      height: 12px;
    }

    &::after {
      height: 12px;
    }
  }

  &::before {
    transition: 0.3s;
    content: '';
    width: 2px;
    height: 22px;
    background: white;
    margin-right: 30px;
  }

  &::after {
    transition: 0.3s;
    content: '';
    width: 2px;
    height: 22px;
    background: white;
    margin-left: 30px;
  }
`;
