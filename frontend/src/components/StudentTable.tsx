import { styled, Table } from '@mui/joy';

import { useAtom } from 'jotai';
import { useGetAllStudentsForSchoolQuery } from '../state/api/students.ts';
import { whoamiAtom } from '../state/api/whoami.ts';

export function StudentTable() {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const students = useGetAllStudentsForSchoolQuery(whoami.schools[0].id);

  return (
    <div style={{ marginTop: '40px', maxHeight: 500, overflow: 'auto', backgroundColor: '#f6fbfa' }}>
      <Table stickyHeader>
        <thead>
          <tr>
            <TableHead>Vorname</TableHead>
            <TableHead>Nachname</TableHead>
            <TableHead>Geburtstag</TableHead>
            <TableHead sx={{ width: '40%' }}>E-Mail</TableHead>
            <TableHead>Klasse</TableHead>
          </tr>
        </thead>
        <tbody>
          {students?.map((student) => (
            <tr key={student.user.id}>
              <TableData>
                {student.user.firstname} {student.user.middlename}
              </TableData>
              <TableData>{student.user.lastname}</TableData>
              <TableData>{student.user.birthDate ?? '-'}</TableData>
              <TableData>{student.user.email}</TableData>
              <TableData>{student.grade.name}</TableData>
            </tr>
          ))}
        </tbody>
      </Table>
    </div>
  );
}

const TableHead = styled('th')`
  background-color: #f6fbfa !important;
  border-bottom: 2px solid #5ec2b7 !important;
  width: 20%;
`;

const TableData = styled('td')`
  border-bottom: 1px solid #89dad5 !important;
`;
