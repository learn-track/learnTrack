import { useQuery } from '@tanstack/react-query';
import { AdminStudentResourceService } from './generated';

export const useGetAllStudentsForSchoolQuery = (schoolId: string) => {
  const { data } = useQuery({
    queryKey: ['studentList', schoolId],
    queryFn: () => AdminStudentResourceService.getAllStudentsWithDetailsBySchoolId(schoolId),
  });

  return data;
};
