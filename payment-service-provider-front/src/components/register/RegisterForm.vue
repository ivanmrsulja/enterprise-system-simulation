<template>
  <v-container>
    <div v-if="!showApiKey">
      <v-row justify="center" class="header-col">
        <v-col cols="3" md="3">
          <h1>Register merchant</h1>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="8" sm="8" md="8" lg="10" xl="10">
          <v-card elevation="4" style="padding: 10px">
            <v-form ref="form" v-model="valid" lazy-validation>
              <v-text-field
                v-model="name"
                :counter="10"
                :rules="nameRules"
                label="Name"
                required
              ></v-text-field>

              <v-text-field
                v-model="merchantId"
                :rules="merchantIdRules"
                label="Merchant ID"
                required
              ></v-text-field>

              <v-text-field
                v-model="merchantPassword"
                :rules="merchantPasswordRules"
                label="Merchant Password"
                required
              ></v-text-field>

              <v-text-field
                v-model="email"
                :rules="emailRules"
                label="Company e-mail"
                required
              ></v-text-field>

              <v-btn
                :disabled="!valid"
                color="success"
                class="mr-4"
                @click="submit"
              >
                Submit
              </v-btn>
            </v-form>
          </v-card>
        </v-col>
      </v-row>
    </div>
    <v-row v-if="showApiKey">
      <v-col cols="5" md="5">
        <h1 style="display: inline-block">Registration</h1>
        <h1 style="color: green; display: inline-block; margin-left: 10px">
          SUCCESSFUL
        </h1>
        <br />
        <br />
        <h1>Your API key is:</h1>
        <h3>{{ apiKey }}</h3>
        <br />
        <br />
        <h2>
          Copy and save it NOW, otherwise you won't be able to access it again!
          You can now login to our platform using your merchant ID and password.
        </h2>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
import { authService } from "../../service/authService";

export default {
  name: "RegisterForm",
  data: () => ({
    valid: true,
    showApiKey: false,
    apiKey: "",
    name: "",
    nameRules: [
      (v) => !!v || "Name is required",
      (v) => (v && v.length <= 50) || "Name must be less than 50 characters",
    ],
    merchantId: "",
    merchantIdRules: [
      (v) => !!v || "Merchant ID is required",
      (v) =>
        (v && v.length == 30) || "Merchant ID must be exactly 30 characters",
    ],
    merchantPassword: "",
    merchantPasswordRules: [
      (v) => !!v || "Merchant password is required",
      (v) =>
        (v && v.length == 100) ||
        "Merchant password must be exactly 100 characters",
    ],
    email: "",
    emailRules: [
      (v) => !!v || "E-mail is required",
      (v) => /.+@.+\..+/.test(v) || "E-mail must be valid",
    ],
  }),

  methods: {
    submit() {
      let payload = {
        name: this.name,
        merchantId: this.merchantId,
        merchantPassword: this.merchantPassword,
        email: this.email,
      };

      authService.registerMerchant(payload).then((response) => {
        if (response.data.apiKey) {
          this.apiKey = response.data.apiKey;
          this.showApiKey = true;
        }
      });
    },
  },
};
</script>

<style scoped>
.header-col {
  text-align: center;
  margin-top: 2%;
  margin-bottom: 0.1%;
}
</style>
