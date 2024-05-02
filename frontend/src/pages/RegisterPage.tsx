import { styled, Typography } from '@mui/joy';

import { RegisterForm } from '../components/RegisterForm.tsx';

export function RegisterPage() {
  return (
    <div style={{ flexGrow: 1, maxWidth: '100%', overflow: 'hidden' }}>
      <Background sx={{ flexGrow: 1, maxWidth: '100%' }}>
        <div style={{ justifyContent: 'center', margin: 'auto' }}>
          <RegisterCard>
            <Typography level="h2" sx={{ fontSize: '45px', marginTop: '40px', textAlign: 'center' }}>
              <Typography sx={{ color: '#3F3D56' }}>Wilkommen bei</Typography>
              <br />
              <Typography sx={{ color: '#5EC2B7' }}>
                Learn<Typography sx={{ color: '#E191A1' }}>Track</Typography>
              </Typography>
            </Typography>
            <RegisterForm />
          </RegisterCard>
        </div>
      </Background>
    </div>
  );
}

const Background = styled('div')`
  background-color: #89dad5;
  height: 100vh;
  display: flex;
  align-items: center;
`;

const RegisterCard = styled('div')`
  background-color: #d6f8f4;
  height: 650px;
  width: 900px;
  margin: auto;
  border-radius: 45px;
  display: flex;
  align-items: center;
  flex-direction: column;
`;
