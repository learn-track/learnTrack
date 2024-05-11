import { CssBaseline, CssVarsProvider, ThemeProvider } from '@mui/joy';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Provider as JotaiProvider } from 'jotai';
import React from 'react';
import ReactDOM from 'react-dom/client';
import { BrowserRouter } from 'react-router-dom';
import { App } from './App.tsx';
import theme from './customTheme.ts';
import './index.css';
import './state/api/interceptors';

const queryClient = new QueryClient();

ReactDOM.createRoot(document.getElementById('root')!).render(
  <React.StrictMode>
    <CssVarsProvider theme={theme}>
      <CssBaseline />
      <ThemeProvider theme={theme}>
        <BrowserRouter>
          <QueryClientProvider client={queryClient}>
            <JotaiProvider>
              <App />
            </JotaiProvider>
          </QueryClientProvider>
        </BrowserRouter>
      </ThemeProvider>
    </CssVarsProvider>
  </React.StrictMode>,
);
