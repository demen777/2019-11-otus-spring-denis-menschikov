import {Configuration} from "./Configuration";
import {checkResponseAndJson, getResponseAndJson} from "../utils/ResponseHandlers";


export class AuthorService {
    constructor() {
        this.config = new Configuration()
    }

    async getAll() {
        return fetch(this.config.AUTHORS_URL)
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }

    async addAuthor(author) {
        console.log("AuthorService.addAuthor author=" + author);
        return fetch(this.config.ADD_AUTHOR_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(author)
        })
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }
}