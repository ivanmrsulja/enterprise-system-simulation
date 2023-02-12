<template>
    <h1 align="center" justify="center">Index application form</h1>
    <br />
    <v-container>
        <v-row align="center" justify="center">
            <v-col cols="12" md="8">
                <v-form @submit.prevent v-model="isFormValid">
                    <v-text-field
                        v-model="name"
                        :rules="rules"
                        label="First Name"
                    ></v-text-field>
                    <v-text-field
                        v-model="surname"
                        :rules="rules"
                        label="Last name"
                    ></v-text-field>
                    <v-text-field
                        v-model="address"
                        :rules="rules"
                        label="Address"
                    ></v-text-field>
                    <v-text-field
                        v-model="education"
                        :rules="rules"
                        label="Education(degree)"
                    ></v-text-field>
                    <v-file-input
                        id="cv"
                        accept=".pdf"
                        label="CV input"
                    ></v-file-input>
                    <v-file-input
                        id="letter"
                        accept=".pdf"
                        label="Cover Letter input"
                    ></v-file-input>
                    <v-btn
                        type="submit"
                        block
                        color="indigo"
                        class="mt-2"
                        @click="submitForm"
                        :disabled="!isFormValid"
                        >Submit</v-btn
                    >
                </v-form>
            </v-col>
        </v-row>
    </v-container>
    <v-snackbar v-model="snackbar" :timeout="snackbarTimeout">
        {{ snackbarText }}

        <v-btn color="blue" text @click="snackbar = false" style="float: right">
            Close
        </v-btn>
    </v-snackbar>
</template>

<script>
import { ref } from "vue";
import { indexService } from "../services/indexService";

export default {
    setup() {
        const name = ref("");
        const surname = ref("");
        const address = ref("");
        const education = ref("");
        const isFormValid = ref(false);

        const snackbar = ref(false);
        const snackbarText = ref("");
        const snackbarTimeout = ref(2000);

        const rules = [
            (value) => {
                if (value.trim()) {
                    return true;
                }

                return "This is a mandatory field.";
            },
        ];

        const submitForm = () => {
            if (
                !name.value.trim() ||
                !surname.value.trim() ||
                !address.value.trim() ||
                !education.value.trim()
            ) {
                snackbarText.value = "All fields are mandatory.";
                snackbar.value = true;
                return;
            }

            let cv = document.querySelector("#cv");
            let letter = document.querySelector("#letter");

            if (cv.files.length === 0 || letter.files.length === 0) {
                snackbarText.value =
                    "You have to upload both CV and Cover Letter .pdf files.";
                snackbar.value = true;
                return;
            }

            let formData = new FormData();
            formData.append("name", name.value.trim());
            formData.append("surname", surname.value.trim());
            formData.append("address", address.value.trim());
            formData.append("education", education.value.trim());
            formData.append("cv", cv.files[0]);
            formData.append("motivationLetter", letter.files[0]);

            indexService
                .indexApplication(formData)
                .then((response) => {
                    if (response.status === 201) {
                        snackbarText.value = "Indexed Successfully.";
                        snackbar.value = true;
                    }
                })
                .catch((reason) => {
                    snackbarText.value = "Error During Indexing.";
                    snackbar.value = true;
                });
        };

        return {
            name,
            surname,
            address,
            education,
            rules,
            isFormValid,
            snackbar,
            snackbarText,
            snackbarTimeout,
            submitForm,
        };
    },
};
</script>
