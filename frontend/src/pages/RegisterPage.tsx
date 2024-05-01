import { Grid, styled, Typography } from '@mui/joy';

import {RegisterForm} from '../components/RegisterForm.tsx';

export function RegisterPage(){
    return(
        <Grid container sx={{ flexGrow: 1, maxWidth: '100%', overflow: 'hidden' }}>
            <Background sx={{ flexGrow: 1, maxWidth: '100%'}}>
                <Grid xs={12} sx={{ position: 'absolute' }}>
                    <RegisterCard>
                        <Typography level="h2" component={'h2'} sx={{ position: 'absolute', fontSize: '45px', marginTop: '5vh', textAlign: 'center'}}>
                            <Typography sx={{ color: '#3F3D56' }}>Wilkommen bei</Typography><br/>
                            <Typography sx={{ color: '#5EC2B7' }}>Learn<Typography sx={{ color: '#E191A1' }}>Track</Typography></Typography>
                        </Typography>
                        <RegisterForm></RegisterForm>
                    </RegisterCard>
                </Grid>
            </Background>
        </Grid>
    );
}

const Background = styled('div')`
  background-color: #89DAD5;
  height: 100vh;
`;

const RegisterCard = styled('div')`
  background-color: #d6f8f4;
  height: 80vh;
  margin: 10vh 20vw;
  border-radius: 45px;
  display: flex;
  align-items: center;
  flex-direction: column;
`;
