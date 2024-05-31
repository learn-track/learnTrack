import { styled, Table } from '@mui/joy';

import { useAtom } from 'jotai';
import { useGetTeachersForSchoolQuery } from '../state/api/teachers.ts';
import { whoamiAtom } from '../state/api/whoami.ts';

export function TeacherTable() {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const teachers = useGetTeachersForSchoolQuery(whoami.schools[0].id);

  return (
    <div style={{ marginTop: '40px', height: 500, overflow: 'auto', backgroundColor: '#f6fbfa' }}>
      <Table stickyHeader>
        <thead>
          <tr>
            <TableHead>Vorname</TableHead>
            <TableHead>Nachname</TableHead>
            <TableHead sx={{ width: '40%' }}>E-Mail</TableHead>
          </tr>
        </thead>
        <tbody>
          {teachers?.map((teacher) => (
            <tr key={teacher.id}>
              <TableData>
                {teacher.firstname} {teacher.middlename}
              </TableData>
              <TableData>{teacher.lastname}</TableData>
              <TableData>{teacher.email}</TableData>
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
