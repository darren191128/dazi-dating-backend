/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{vue,js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        primary: {
          DEFAULT: '#1a3a5f',
          dark: '#0f2944',
          light: '#2a4a6f'
        },
        accent: {
          DEFAULT: '#c9a227',
          dark: '#a88220',
          light: '#d4b43d'
        }
      },
      fontFamily: {
        sans: ['Inter', 'system-ui', 'sans-serif'],
        serif: ['Noto Serif SC', 'serif']
      }
    },
  },
  plugins: [],
}
