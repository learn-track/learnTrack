import { Lock, Person } from '@mui/icons-material';
import { Button, Input, Link, Stack, styled } from '@mui/joy';

export function LoginForm() {
  return (
    <>
      <form
        onSubmit={(event) => {
          event.preventDefault();
          console.log(event.target);
        }}>
        <Stack spacing={3} width={350}>
          <Input
            startDecorator={
              <Person
                sx={{
                  fontSize: '25px',
                  color: '#333',
                  borderRight: '2px solid #5EC2B7',
                  width: '50px',
                  height: '20px',
                }}
              />
            }
            placeholder={'Your Username'}
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
            placeholder={'Your Password'}
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
              Forgot Password
            </Link>
          </LinkContainer>

          <Button type="submit">Login now</Button>
        </Stack>
      </form>
    </>
  );
}
const LinkContainer = styled('div')`
  width: 50%;
`;
