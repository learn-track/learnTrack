import { keyframes } from '@emotion/react';
import { Grid, Link, styled, Typography } from '@mui/joy';
import { LoginForm } from '../components/LoginForm.tsx';
import { Logo } from '../components/Logo.tsx';

export function LoginPage() {
  return (
    <>
      <Grid container sx={{ flexGrow: 1, maxWidth: '100%', overflow: 'hidden' }}>
        <Grid xs={5.5} sx={{ position: 'relative' }}>
          <LeftSide>
            <Typography level="h1" component={'h1'} sx={{ marginBottom: '50px' }}>
              <Typography sx={{ color: '#E191A1' }}>Log</Typography> in
            </Typography>
            <LoginForm></LoginForm>
          </LeftSide>
          <PrimaryCircle sx={{ top: 0, transform: 'translate(-30%, -40%)' }}>
            <Link href="#" sx={{ marginTop: '70px', marginLeft: `70px`, color: 'white' }}>
              Go back to Home
            </Link>
          </PrimaryCircle>
          <PrimaryCircle sx={{ bottom: 0, right: 0, transform: 'translate(30%, 40%)' }} />
          <SecondaryCircle1
            sx={{ bottom: 45, right: '70%', animation: '${`floatAnimation2`} 3s ease-in-out reverse infinite' }}>
            <img src="../assets/login/certificate-icon.svg" alt="Icon" />
          </SecondaryCircle1>

          <SecondaryCircle2
            sx={{ top: 45, left: '70%', animation: '${`floatAnimation`} 3s ease-in-out reverse infinite' }}>
            <img src="../assets/login/graduation-hat-icon.svg" alt="Icon" />
          </SecondaryCircle2>
        </Grid>
        <Grid xs={6.5}>
          <RightSide>
            <Logo />
            <img src="../assets/login/learn-image.svg" width={'85%'} alt="Image" />
            <Typography
              level="h2"
              component={'h2'}
              sx={{ marginBottom: '50px', color: `#E191A1`, alignSelf: `start`, zIndex: 99 }}>
              <Typography sx={{ color: '#D6F8F4' }}>Time to</Typography> Learn
            </Typography>
          </RightSide>
        </Grid>
      </Grid>
      {/*
        <Typography level="h1" component={'h1'}>
        Log in
      </Typography>

      <Button color="primary">Button</Button>
      <Button disabled>Disabled</Button>
      <ImageContainer></ImageContainer>*/}
    </>
  );
}

const floatAnimation = keyframes`
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(-10px) translateX(-4px); /* Adjust this value as needed */
  }
  100% {
    transform: translateY(0);
  }
`;

const floatAnimation2 = keyframes`
  0% {
    transform: translateY(0);
  }
  50% {
    transform: translateY(5px) translateX(4px); /* Adjust this value as needed */
  }
  100% {
    transform: translateY(0);
  }
`;

const RightSide = styled('div')`
  background-color: #5ec2b7;
  min-height: 100vh;
  padding: 2vh 5vw;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
`;

const LeftSide = styled('div')`
  background-color: #d6f8f4;
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
`;

const PrimaryCircle = styled('div')`
  position: absolute;
  width: 300px;
  height: 300px;
  border-radius: 50%;
  background-color: #5ec2b7;
  z-index: 0;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const SecondaryCircleStandard = styled('div')`
  position: absolute;
  width: 130px;
  height: 130px;
  border-radius: 50%;
  background-color: #e191a1;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const SecondaryCircle1 = styled(SecondaryCircleStandard)`
  bottom: 45px;
  right: 70%;
  animation: ${floatAnimation} 3s ease-in-out reverse infinite;
`;

const SecondaryCircle2 = styled(SecondaryCircleStandard)`
  top: 45px;
  left: 70%;
  animation: ${floatAnimation2} 3s ease-in-out reverse infinite;
`;
