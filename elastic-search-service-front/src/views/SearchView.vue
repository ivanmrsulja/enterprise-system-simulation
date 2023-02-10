<template>
    <v-container>
        <v-row cols="11">
            <p align="center">
                Enter query tokens as follows: {region}:{value}. Supported
                operators are AND, OR and NOT. Supported application regions
                are: name, surname, education, address, cv and letter. If you
                don't enter an operator the default 'OR' operator will be used.
            </p>
        </v-row>
        <v-row>
            <v-col cols="11">
                <vue3-tags-input
                    :tags="tags"
                    placeholder="Enter search query"
                    @on-tags-changed="handleChangeTag"
                    @on-remove="handleRemoveTag"
                />
            </v-col>
            <v-col cols="1">
                <v-btn
                    color="pink"
                    style="margin-top: -1px; margin-left: -20px"
                    @click="search()"
                >
                    <v-icon>mdi-magnify</v-icon>
                </v-btn>
            </v-col>
        </v-row>
        <v-row align="center" justify="center">
            <v-col cols="12" sm="6" md="3">
                <v-row cols="12" sm="6" md="3">
                    <v-text-field
                        label="Add phrase"
                        placeholder="Insert phrase (<region>:<phrase>)"
                        v-model="phrase"
                    ></v-text-field>
                    <v-btn
                        class="mx-2"
                        fab
                        dark
                        color="indigo"
                        style="margin-top: 10px"
                        @click="addPhrase()"
                    >
                        <v-icon dark> mdi-plus </v-icon>
                    </v-btn>
                </v-row>
            </v-col>
            <v-col cols="12" sm="6" md="3">
                <v-text-field
                    label="Add center point for geolocation"
                    placeholder="Enter city name"
                    v-model="city"
                ></v-text-field>
            </v-col>
            <v-col cols="12" sm="6" md="3">
                <v-text-field
                    label="Distance"
                    type="Number"
                    placeholder="Enter distance in kM"
                    min="0"
                    v-model="distance"
                ></v-text-field>
            </v-col>
        </v-row>
        <v-row v-for="(result, index) in searchResults" v-bind:key="index">
            <v-card class="mx-auto" style="margin-top: 10px; width: 1000px">
                <v-card-text>
                    <div>Candidate</div>
                    <p class="text-h4 text--primary">
                        {{ result.name }} {{ result.surname }}
                    </p>
                    <p>Education: {{ result.education }}</p>
                    <br />
                    <p>Location: {{ result.address }}</p>
                    <br />
                    <div class="text--primary">
                        <p v-if="result.highlights.cv">CV:</p>
                        <p
                            v-for="(cvHighlight, cvHighlightIndex) in result
                                .highlights.cv"
                            v-bind:key="cvHighlightIndex"
                            v-html="cvHighlight"
                        ></p>
                        <br />
                        <p v-if="result.highlights.letter">Letter:</p>
                        <p
                            v-for="(
                                letterHighlight, letterHighlightIndex
                            ) in result.highlights.letter"
                            v-bind:key="letterHighlightIndex"
                            v-html="letterHighlight"
                        ></p>
                    </div>
                </v-card-text>
                <v-card-actions>
                    <v-btn text color="deep-purple accent-4">
                        Download CV
                    </v-btn>
                    <v-btn text color="deep-purple accent-4">
                        Download cover letter
                    </v-btn>
                </v-card-actions>
            </v-card>
        </v-row>
    </v-container>
</template>

<script>
import { ref } from "vue";
import Vue3TagsInput from "vue3-tags-input";
import { searchService } from "../services/searchService";

export default {
    components: {
        Vue3TagsInput,
    },
    setup() {
        const tags = ref([]);
        const phrase = ref("");
        const city = ref("");
        const distance = ref(0);
        const defaultOperator = "OR";
        const searchResults = ref([]);

        const handleChangeTag = (tagList) => {
            if (tagList.length === 0) {
                return;
            }

            tags.value = tagList;
            console.log(tags.value);
            let potential_operator =
                tags.value[tags.value.length - 1].toUpperCase();

            if (
                potential_operator === "AND" ||
                potential_operator === "OR" ||
                potential_operator === "NOT"
            ) {
                tags.value[tags.value.length - 1] = potential_operator;
                return;
            } else if (!potential_operator.includes(":")) {
                tags.value.pop();
                return;
            }

            tags.value[tags.value.length - 1] =
                tags.value[tags.value.length - 1].toLowerCase();
        };

        const handleRemoveTag = (tagIndex) => {
            tags.value.splice(tagIndex, 1);
        };

        const addPhrase = () => {
            let phraseTokens = phrase.value.split(":");
            tags.value.push(
                phraseTokens[0].trim() + ':"' + phraseTokens[1].trim() + '"'
            );
            phrase.value = "";
        };

        const search = () => {
            let operators = ["AND", "OR", "NOT"];
            let lastTokenOperator = false;
            let searchQuery = [];
            for (let [index, token] of tags.value.entries()) {
                if (operators.includes(token)) {
                    if (lastTokenOperator || index === 0) {
                        console.log();
                        alert("aaa");
                        return;
                    }
                    lastTokenOperator = true;
                    searchQuery.push(token);
                    continue;
                }

                if (!lastTokenOperator && index > 0) {
                    searchQuery.push(defaultOperator);
                }
                lastTokenOperator = false;
                searchQuery.push(token);
            }
            console.log(searchQuery);
            let payload = {
                expression: searchQuery,
                cityName: city.value,
                distance: distance.value,
            };
            searchService.search(payload).then((response) => {
                searchResults.value = response.data;
            });
        };

        return {
            tags,
            phrase,
            city,
            distance,
            searchResults,
            handleChangeTag,
            handleRemoveTag,
            addPhrase,
            search,
        };
    },
};
</script>

<style>
em {
    color: red;
}
</style>
