<template>
    <vue3-tags-input
        :tags="tags"
        placeholder="Enter search query"
        @on-tags-changed="handleChangeTag" />
</template>

<script>
import { ref } from "vue";
import Vue3TagsInput from "vue3-tags-input";

export default {
    components: {
        Vue3TagsInput,
    },
    setup() {
        const tags = ref(['"Ivan Kotor"']);

        const handleChangeTag = (tag_list) => {
            tags.value = tag_list;
            let potential_operator =
                tags.value[tags.value.length - 1].toUpperCase();
            if (
                potential_operator === "AND" ||
                potential_operator === "OR" ||
                potential_operator === "NOT"
            ) {
                tags.value[tags.value.length - 1] = potential_operator;
            }
        };

        return {
            tags,
            handleChangeTag,
        };
    },
};
</script>
