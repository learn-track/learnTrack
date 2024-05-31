import { useQuery } from '@tanstack/react-query';
import { AdminTeacherResourceService } from './generated';

export const useGetTeachersForSchoolQuery = (schoolId: string) => {
  const { data } = useQuery({
    queryKey: ['teacherList', schoolId],
    queryFn: () => AdminTeacherResourceService.getAllTeachersForSchool(schoolId),
  });

  return data;
};
