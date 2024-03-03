import { styled } from '@mui/joy';
import { Route, Routes } from 'react-router-dom';
import { Header } from './components/Header.tsx';
import { LandingPage } from './pages/LandingPage.tsx';

export function App() {
  const Wrapper = styled('div')`
    min-height: 100vh;
    display: grid;
    grid-template-rows: auto 1fr;
  `;

  return (
    <Wrapper>
      <Header />
      <Routes>
        <Route path={'/'} element={<LandingPage />} />
      </Routes>
    </Wrapper>
  );
}
