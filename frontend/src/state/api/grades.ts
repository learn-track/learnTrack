import { useQuery } from '@tanstack/react-query';
import { AdminGradeResourceService } from './generated';

export const useGetGradesForSchoolQuery = (schoolId: string) => {
  const { data } = useQuery({
    queryKey: ['gradeList', schoolId],
    queryFn: () => AdminGradeResourceService.getAllGradesWithInfosBySchoolId(schoolId),
  });
  return data;
};
