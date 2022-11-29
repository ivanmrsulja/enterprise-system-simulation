<template>
    <v-container>
        <v-row align="center" justify="center">
            <v-col cols="6" md="6" class="header-col">
            <h1>Login</h1>
            </v-col>
        </v-row>
        <form @submit.prevent="login">
            <v-row align="center" justify="center">
            <v-col cols="12" sm="8" md="8" lg="6" xl="4">
                <v-card elevation="4" style="padding: 10px;">
                <v-row align="center" justify="center">
                    <v-col cols="8" sm="8" md="8" lg="10" xl="10">
                    <v-text-field label="Merchant ID" required v-model="payload.merchantId">
                    </v-text-field>
                    </v-col>
                </v-row>
                <v-row align="center" justify="center">
                    <v-col cols="8" sm="8" md="8" lg="10" xl="10">
                    <v-text-field
                        label="Merchant Password"
                        :type="valuePassword ? 'password' : 'text'"
                        @click:append="() => (valuePassword = !valuePassword)"
                        :append-icon="valuePassword ? 'mdi-eye-off' : 'mdi-eye'"
                        required
                        v-model="payload.merchantPassword"
                    >
                    </v-text-field>
                    </v-col>
                </v-row>
                <v-row v-if="secondStep === true" align="center" justify="center">
                    <v-col cols="8" sm="8" md="8" lg="10" xl="10">
                        <v-text-field label="Enter code from email" required v-model="authCode"></v-text-field>
                    </v-col>
                </v-row>
    
                <v-row align="center" justify="center">
                    <v-col cols="8" sm="8" md="8" lg="10" xl="10">
                    <v-btn
                        depressed
                        color="primary"
                        type="submit"
                        :disabled="payload.merchantId === '' || payload.merchantPassword === ''"
                        :loading="loginBtnLoading"
                    >
                        Login
                    </v-btn>
                    </v-col>
                </v-row>
                </v-card>
            </v-col>
            </v-row>
        </form>
        <v-snackbar v-model="snackbar" :timeout="timeout">
            {{ text }}
    
            <template v-slot:action="{ attrs }">
            <v-btn color="blue" text v-bind="attrs" @click="snackbar = false">
                Close
            </v-btn>
            </template>
        </v-snackbar>
    </v-container>
  </template>
  
  <script>
  import { authService } from "../../service/authService";
  import jwt_decode from "jwt-decode";

  export default {
    name: "LoginForm",
    data: () => {
      return {
        valuePassword: String,
        authCode: "",
        secondStep: false,
        payload: {
          merchantId: "",
          merchantPassword: "",
        },
        loginBtnLoading: false,
        snackbar: false,
        text: "Wrong username/password combination.",
        timeout: 5000,
      };
    },
    methods: {
      login() {
        if (this.secondStep) {
            this.loginSecondStep();
            return;
        }
        this.loginBtnLoading = true;
        authService
            .login(this.payload)
            .then((response) => {
                this.loginBtnLoading = false;
                this.secondStep = true;
                this.text = "We have sent you a 2FA code. Check your email and enter the code in the given box.";
                this.snackbar = true;
          })
          .catch((error) => {
                if (error.response) this.text = error.response.data.message;
                this.snackbar = true;
                this.loginBtnLoading = false;
          });
      },
      loginSecondStep() {
        this.loginBtnLoading = true;
        authService
            .loginSecondStep({merchantId: this.payload.merchantId, pinCode: this.authCode})
            .then((response) => {
                let decodedToken = jwt_decode(response.data.token);
                if (decodedToken.roles === "ROLE_MERCHANT") {
                    authService.setToken(response.data.token);
                    this.emitter.emit("loginSuccess", decodedToken.roles);
                    this.$router.push({ name: "paymentMethods" });
                } else {
                    this.text = "Wrong username/password combination.";
                    this.snackbar = true;
                    this.loginBtnLoading = false;
                }
          })
          .catch((error) => {
                if (error.response) this.text = error.response.data.message;
                this.text = "Check your 2FA code and try again.";
                this.snackbar = true;
                this.loginBtnLoading = false;
          });
      }
    },
  };
  </script>
  
  <style scoped>
  .header-col {
    text-align: center;
    margin-top: 2%;
  }
  </style>