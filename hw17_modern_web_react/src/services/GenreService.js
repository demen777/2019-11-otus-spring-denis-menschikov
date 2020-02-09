import {Configuration} from "./Configuration";
import {checkResponseAndJson, getResponseAndJson} from "../utils/ResponseHandlers";


export class GenreService {
    constructor() {
        this.config = new Configuration()
    }

    async getAll() {
        return fetch(this.config.GENRES_URL)
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }

    async addGenre(genreName) {
        console.log("GenreService.addGenre genreName=" + genreName);
        const genre = {name: genreName};
        return fetch(this.config.ADD_GENRE_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(genre)
        })
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }
}