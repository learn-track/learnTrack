import { Lock, Mail } from '@mui/icons-material';
import { Button, Input, Link, Stack, styled } from '@mui/joy';

export function LoginForm() {
  return (
    <form
      onSubmit={(event) => {
        event.preventDefault();
        console.log(event.target);
      }}>
      <Stack spacing={3} width={350}>
        <Input
          startDecorator={
            <Mail
              sx={{
                fontSize: '25px',
                color: '#333',
                borderRight: '2px solid #5EC2B7',
                width: '50px',
                height: '20px',
              }}
            />
          }
          placeholder={'E-Mail'}
          sx={{
            background: 'rgba(137,218,213,0.5)',
            padding: '12px 20px',
            fontFamily: 'Montserrat, sans-serif',
            fontSize: '15px',
          }}
          variant="plain"
          size={'lg'}
        />

        <Input
          type={'password'}
          startDecorator={
            <Lock
              sx={{
                fontSize: '25px',
                color: '#333',
                borderRight: '2px solid #5EC2B7',
                width: '50px',
                height: '20px',
              }}
            />
          }
          placeholder={'Passwort'}
          sx={{
            background: 'rgba(137,218,213,0.5)',
            padding: '12px 20px',
            fontFamily: 'Montserrat, sans-serif',
            fontSize: '15px',
          }}
          variant="plain"
          size={'lg'}
        />
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
          {/* @todo implement login functionality */}
        </LinkContainer>

        <Button type="submit">Anmelden</Button>
      </Stack>
    </form>
  );
}

const LinkContainer = styled('div')`
  width: 75%;
`;
