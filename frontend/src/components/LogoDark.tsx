import { Link, styled, Typography } from '@mui/joy';
import LearnTrackLogo from '../assets/logo-dark.svg?react';

export function LogoDark() {
  return (
    <LogoContainer>
      <CustomLink href={'/grades'}>
        <LearnTrackLogo width={'60px'} />
        <Typography
          level="h2"
          noWrap={true}
          component={'h2'}
          sx={{ color: `#E191A1`, marginLeft: '20px', fontSize: '35px' }}>
          <Typography sx={{ color: '#5EC2B7' }}>Learn</Typography> Track
        </Typography>
      </CustomLink>
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
const CustomLink = styled(Link)`
  &::before {
    display: none;
  }

  &:hover {
    &::before {
      width: 0;
    }
  }
`;
