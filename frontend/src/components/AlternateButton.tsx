import { Button, styled } from '@mui/joy';

export const AlternateButton = styled(Button)`
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

  &:hover {
    background-color: #e191a1;
    &::before {
      height: 12px;
    }

    &::after {
      height: 12px;
    }
  }
`;
