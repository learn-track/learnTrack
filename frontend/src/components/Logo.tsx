import { styled, Typography } from '@mui/joy';

export function Logo() {
  return (
    <>
      <LogoContainer>
        <img src="../assets/learnup-logo.svg" width={'70px'} alt="Logo Icon" />
        <Typography level="h2" component={'h2'} sx={{ color: `#E191A1`, marginLeft: '20px' }}>
          <Typography sx={{ color: '#89DAD5' }}>Learn</Typography> UP
        </Typography>
      </LogoContainer>
    </>
  );
}

const LogoContainer = styled('div')`
  align-self: end;
  padding: 20px 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
`;
