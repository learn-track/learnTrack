import { useQuery } from '@tanstack/react-query';
import { AdminTeacherResourceService } from './generated';

export const useGetTeachersForSchoolQuery = (schoolId: string) => {
  const { data } = useQuery({
    queryKey: ['teacherList', schoolId],
    queryFn: () => AdminTeacherResourceService.getAllTeachersForSchool(schoolId),
  });

  return data;
};

export const useSearchTeacherByEmailQuery = (schoolId: string, searchText: string, enabled: boolean) => {
  return useQuery({
    queryKey: ['teacherSearch', searchText],
    queryFn: () => AdminTeacherResourceService.searchTeacherByEmail(schoolId, searchText),
    enabled: enabled,
  });
};
