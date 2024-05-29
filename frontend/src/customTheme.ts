import { extendTheme } from '@mui/joy/styles';

const primaryColor = '#5EC2B7';

export default extendTheme({
  fontFamily: {
    display: 'Montserrat',
    body: 'Montserrat',
  },
  colorSchemes: {
    light: {
      palette: {
        primary: {
          mainChannel: 'var(--joy-palette-primary-400)',
          50: '#C0CCD9',
          100: '#D6F8F4',
          200: '#1dd9cd',
          300: '#5ae0d7',
          400: '#89DAD5',
          500: '#5EC2B7',
          600: '#79BCB8',
          700: '#5ab0ac',
          800: '#568c8a',
          900: '#2f4c3d',
        },
      },
    },
  },
  typography: {
    h1: {
      fontFamily: 'Montserrat, sans-serif',
      fontWeight: 630,
      fontSize: '96px',
      color: primaryColor,
    },
    h2: {
      fontFamily: 'Montserrat, sans-serif',
      fontWeight: 630,
      fontSize: '74px',
      color: primaryColor,
    },
    h3: {
      fontFamily: 'Montserrat, sans-serif',
      fontWeight: 500,

      fontSize: '50px',
    },
    h4: {
      fontFamily: 'Montserrat, sans-serif',
      fontWeight: 360,

      fontSize: '34px',
    },
  },
  components: {
    JoyButton: {
      styleOverrides: {
        root: {
          fontFamily: 'Montserrat, sans-serif',
          fontWeight: '300',
          fontSize: '18px',
          padding: '8px 20px',
          transition: '0.2s',
        },
      },
    },
    JoyLink: {
      styleOverrides: {
        root: {
          fontFamily: 'Montserrat, sans-serif',
          fontWeight: 400,
          fontSize: '16px',
          color: '#000',
          position: 'relative',
          transition: ' 0.5s',
          '&::before': {
            content: '""',
            position: 'absolute',
            bottom: -5,
            left: 0,
            width: '100%',
            height: '4px',
            backgroundColor: '#fff',
            transition: ' 0.5s',
          },
          '&:hover': {
            textDecoration: 'none',

            '&::before': {
              content: '""',
              position: 'absolute',
              bottom: -5,
              left: 0,
              width: '80%',
              height: '4px',
              backgroundColor: '#E191A1',
            },
          },
        },
      },
    },
  },
});
declare module '@mui/joy/styles' {}
