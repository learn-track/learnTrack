import { useQuery } from '@tanstack/react-query';
import { AdminGradeResourceService } from './generated';

export const useGetGradesForSchoolQuery = (schoolId: string) => {
  const { data } = useQuery({
    queryKey: ['gradeList', schoolId],
    queryFn: () => AdminGradeResourceService.getAllGradesWithDetailsBySchoolId(schoolId),
  });
  return data;
};
