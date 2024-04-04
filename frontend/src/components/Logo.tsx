import { styled, Typography } from '@mui/joy';
import LearnTrackLogo from '../assets/logo.svg?react';

export function Logo() {
  return (
    <LogoContainer>
      <LearnTrackLogo width={'80px'} />
      <Typography level="h2" component={'h2'} sx={{ color: `#E191A1`, marginLeft: '20px' }}>
        <Typography sx={{ color: '#89DAD5' }}>Learn</Typography> Track
      </Typography>
    </LogoContainer>
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
