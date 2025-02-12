import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import path from 'path';

export default defineConfig({
  plugins: [vue()],
  resolve: {
    alias: {
      '@': path.resolve(__dirname, 'src'), // Ensures @ points to the src folder
    },
  },
  css: {
    postcss: './tailwind.config.js', // Ensure Vite loads PostCSS
  }
})
