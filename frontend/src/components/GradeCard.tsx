import EditIcon from '@mui/icons-material/Edit';
import PeopleOutlineIcon from '@mui/icons-material/PeopleOutline';
import SchoolOutlinedIcon from '@mui/icons-material/SchoolOutlined';

import { Button, Card, CardContent, Divider, styled, Typography } from '@mui/joy';

interface GradeCardProps {
  className: string;
  teacherCount: number;
  studentCount: number;
}

export function GradeCard({ className, teacherCount, studentCount }: GradeCardProps) {
  return (
    <GradeCardContainer>
      <CardContent sx={{ justifyContent: 'flex-end', padding: '5px' }}>
        <EditButton className="editButton" variant={'plain'}>
          <EditIcon className="editIcon" style={{ fontSize: 27 }} />
        </EditButton>
        <Typography level="h4" marginTop={4} marginBottom={1} textOverflow="ellipsis" sx={{ color: '#2b2929' }}>
          {className}
        </Typography>
        <MoreDetailsWrapper>
          <DetailContainer>
            <SchoolOutlinedIcon style={{ fontSize: 24, color: 'black' }} />
            {teacherCount}
          </DetailContainer>
          <CustomDivider />
          <DetailContainer>
            <PeopleOutlineIcon style={{ fontSize: 24, color: 'black' }} />
            {studentCount}
          </DetailContainer>
        </MoreDetailsWrapper>
      </CardContent>
    </GradeCardContainer>
  );
}
const GradeCardContainer = styled(Card)`
  border: 1px solid #e0f3f2;
  box-shadow: 0 0 2px 0 rgba(222, 227, 226, 0.47);
  border-radius: 25px;
  width: 360px;
  transition: 0.4s;
  cursor: pointer;

  &:hover {
    transform: translateY(-10px);
    transition: 0.4s;
    box-shadow: 0 10px 9px 3px rgba(222, 227, 226, 0.31);

    .editButton {
      transition: 0.4s;
      background: #3cc2b1;
    }

    .editIcon {
      transition: 0.4s;
      color: white !important;
    }
  }
`;

const EditButton = styled(Button)`
  display: flex;
  align-self: end;
  justify-self: end;
  align-items: center;
  justify-content: space-around;
  font-size: 47px;
  padding: 10px 11px;
  width: fit-content;
  border: 1px solid #3cc2b1;

  border-radius: 15px;

  &:hover {
    background: #3cc2b1;
    & .MuiSvgIcon-root {
      color: #3cc2b1 !important;
    }
  }
`;

const MoreDetailsWrapper = styled('div')`
  display: flex;
  align-self: end;
  justify-self: end;
  align-items: center;
  justify-content: space-around;
  width: 100%;
  padding: 15px 12px;
  border: #52a49b 1px solid;
  border-radius: 15px;
`;
const CustomDivider = styled(Divider)`
  height: 25px;
  width: 3px;
  border-radius: 5px;

  background: #89dad5;
`;

const DetailContainer = styled('div')`
  display: flex;
  align-items: center;
  justify-content: space-around;
  width: 74px;
  font-size: 18px;
  font-weight: 700;
`;
