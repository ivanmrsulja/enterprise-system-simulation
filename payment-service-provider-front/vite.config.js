import { fileURLToPath, URL } from "node:url";
import mkcert from "vite-plugin-mkcert";
import fs from "fs";

import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

// https://github.com/vuetifyjs/vuetify-loader/tree/next/packages/vite-plugin
import vuetify from "vite-plugin-vuetify";

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [vue(), vuetify({ autoImport: true })],
  resolve: {
    alias: {
      "@": fileURLToPath(new URL("./src", import.meta.url)),
    },
  },
  server: {
    https: {
      key: fs.readFileSync("certificates/selfsigned_key.pem"),
      cert: fs.readFileSync("certificates/selfsigned.pem"),
    },
  },
});
