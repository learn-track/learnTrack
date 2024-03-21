import { CssBaseline, CssVarsProvider, styled, ThemeProvider } from '@mui/joy';
import { Route, Routes, useLocation } from 'react-router-dom';
import { Header } from './components/Header.tsx';
import theme from './customTheme.ts';
import { LandingPage } from './pages/LandingPage.tsx';
import { LoginPage } from './pages/LoginPage.tsx';

export function App() {
  const Wrapper = styled('div')`
    min-height: 100vh;
    display: grid;
    grid-template-rows: auto 1fr;
  `;

  const location = useLocation();
  const showHeader: boolean = location.pathname !== '/login';

  return (
    <CssVarsProvider theme={theme}>
      <CssBaseline />
      <ThemeProvider theme={theme}>
        <Wrapper>
          {showHeader && <Header />}
          <Routes>
            <Route path={'/'} element={<LandingPage />} />
            <Route path={'/login'} element={<LoginPage />} />
          </Routes>
        </Wrapper>
      </ThemeProvider>
    </CssVarsProvider>
  );
}
