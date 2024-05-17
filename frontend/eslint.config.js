import ts from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';
import prettier from 'eslint-config-prettier';
import reactJsxRuntime from 'eslint-plugin-react/configs/jsx-runtime.js';
import reactRecommended from 'eslint-plugin-react/configs/recommended.js';
import globals from 'globals';

export default [
  {
    ignores: [
      'vite.config.ts',
      'vitest.config.ts',
      'eslint.config.js',
      'dist',
      'coverage',
      'sentry-upload-source-maps.js',
      '.yarn',
      'playwright.config.ts',
      'lostpixel.config.ts',
    ],
  },
  reactRecommended,
  reactJsxRuntime,
  { settings: { react: { version: 'detect' } } },
  {
    files: ['**/*.{js,jsx,mjs,cjs,ts,tsx}'],
    languageOptions: {
      parser: tsParser,
      parserOptions: {
        ecmaFeatures: { modules: true },
        ecmaVersion: 'latest',
        project: './tsconfig.json',
      },
      globals: {
        ...globals.browser,
      },
    },
    plugins: {
      '@typescript-eslint': ts,
      ts,
    },
    rules: {
      ...ts.configs['eslint-recommended'].rules,
      ...ts.configs['recommended'].rules,
      ...ts.configs['recommended-requiring-type-checking'].rules,
      ...prettier.rules,
      '@typescript-eslint/ban-types': [
        2,
        {
          types: {
            any: "Don't use 'any' because it is unsafe",
          },
          extendDefaults: true,
        },
      ],
      '@typescript-eslint/restrict-template-expressions': [0],
      '@typescript-eslint/no-misused-promises': [
        'error',
        {
          checksVoidReturn: false,
        },
      ],
      '@typescript-eslint/no-unused-vars': [
        'warn', // or "error"
        {
          argsIgnorePattern: '^_',
          varsIgnorePattern: '^_',
          caughtErrorsIgnorePattern: '^_',
        },
      ],
    },
  },
];
