import SettingsIcon from '@mui/icons-material/Settings';
import { Button, Divider, Dropdown, Menu, MenuButton, MenuItem, styled } from '@mui/joy';
import { useAtom } from 'jotai';
import { NavLink } from 'react-router-dom';
import { whoamiAtom } from '../state/api/whoami.ts';
import { LogoDark } from './LogoDark.tsx';

export function Header() {
  const [{ data: whoami }] = useAtom(whoamiAtom);
  const HandleLogout = () => {
    localStorage.removeItem('token');
    window.location.href = '/login';
  };

  return (
    <HeaderContainer>
      <SideItems>
        <LogoDark />
      </SideItems>
      <NavigationItems>{whoami.user.userRole == 'ADMIN' && <AdminHeaderLinks />}</NavigationItems>
      <SideItems sx={{ justifyContent: 'center' }}>
        <Dropdown>
          <UserMenuButton>
            {whoami.user.firstname} {whoami.user.lastname}
          </UserMenuButton>
          <UserMenu>
            <UserDetails>
              <TopDescription>
                <TopDescriptionTextWrapper>Angemeldet als</TopDescriptionTextWrapper>

                <TopDescriptionEmailWrapper>{whoami.user.email}</TopDescriptionEmailWrapper>
              </TopDescription>

              <SettingsButton variant="outlined">
                <SettingsIcon />
              </SettingsButton>
            </UserDetails>
            <CustomDivider />

            {whoami.user.userRole == 'TEACHER' && <TeacherUserMenu />}
            {whoami.user.userRole == 'STUDENT' && <StudentUserMenu />}

            <MenuItem>
              <Button color={'danger'} variant="outlined" sx={{ minWidth: '100%' }} onClick={HandleLogout}>
                Abmelden
              </Button>
            </MenuItem>
          </UserMenu>
        </Dropdown>
      </SideItems>
    </HeaderContainer>
  );
}

function AdminHeaderLinks() {
  return (
    <>
      <HeaderLink to={'grades'}>Klassen</HeaderLink>
      <HeaderLink to={'students'}>Schüler</HeaderLink>
      <HeaderLink to={'teachers'}>Lehrer</HeaderLink>
    </>
  );
}
function TeacherUserMenu() {
  return (
    <>
      <MenuItem>Zürich</MenuItem>
      <MenuItem>Winterhur</MenuItem>
      <MenuItem>Uster</MenuItem>
      <CustomDivider />
    </>
  );
}
function StudentUserMenu() {
  return (
    <>
      <MenuItem>Mathe</MenuItem>
      <MenuItem>Deutsch</MenuItem>
      <MenuItem>English</MenuItem>
      <CustomDivider />
    </>
  );
}

const HeaderContainer = styled('div')`
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 5px 20px;
  background-color: #fdf9f9;
  box-shadow: 0 6px 14px 3px #ececec;
  height: 100px;
`;

const NavigationItems = styled('div')`
  display: flex;
  align-items: center;
  justify-content: space-around;
  justify-self: center;
  min-width: 40%;
`;
const UserDetails = styled(MenuItem)`
  background-color: white;
  &:hover {
    background-color: white !important;
  }
`;

const TopDescription = styled('span')`
  display: flex;

  justify-content: space-between;

  flex-direction: column;
`;
const TopDescriptionTextWrapper = styled('span')`
  font-size: 0.9rem;
  color: #828282;
`;
const TopDescriptionEmailWrapper = styled('span')`
  text-wrap: none;
  font-size: 1rem;
  margin-right: 10px;
`;

const SettingsButton = styled(Button)`
  display: flex;
  align-items: center;
  justify-content: space-around;
  font-size: 17px;
  padding: 10px;
`;

const CustomDivider = styled(Divider)`
  margin: 12px 0;
  background: #c7e3dc;
`;

const HeaderLink = styled(NavLink)`
  display: flex;
  align-items: center;
  justify-content: space-around;
  font-size: 22px;
  position: relative;

  font-family: Montserrat, sans-serif;
  text-decoration: none;
  color: #000000;

  &::before {
    position: absolute;
    content: '';

    bottom: -5px;

    width: 0;
    height: 4px;
    background: transparent;
    transition: 0.5s;
  }

  &:hover::before,
  &.active::before {
    position: absolute;
    content: '';
    bottom: -5px;

    width: 80%;
    height: 4px;
    background: #5ec2b7;
    transition: 0.5s;
  }
`;

const UserMenuButton = styled(MenuButton)`
  min-width: 30%;
  font-size: 22px;

  font-weight: 400;
  border: #7ad1c7 1px solid;
`;
const UserMenu = styled(Menu)`
  font-size: 20px;
  font-weight: 300;
  filter: drop-shadow(0px 6px 8px rgba(203, 251, 246, 0.46));
`;

const SideItems = styled('div')`
  display: flex;
  align-items: center;
  min-width: 30%;
`;
