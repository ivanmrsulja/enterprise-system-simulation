<template>
  <v-app>
    <v-main>
      <NavbarVue></NavbarVue>
      <router-view/>
    </v-main>
  </v-app>
</template>

<script>
import NavbarVue from './components/Navbar.vue';

export default {
  name: 'App',

  data: () => ({
    //
  }),

  components: {
    NavbarVue,
  },
}
</script>
