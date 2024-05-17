export interface UserData {
  email: string;
  password: string;
}

// Same users as in db seed file
export const testUser: { [role: string]: UserData } = {
  admin: {
    email: 'admin@gmail.com',
    password: 'test',
  },
  teacher: {
    email: 'teacher@gmail.com',
    password: 'test',
  },
  student: {
    email: 'student@gmail.com',
    password: 'test',
  },
};
