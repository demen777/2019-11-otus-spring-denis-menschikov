import {Configuration} from "./Configuration";

export class GenreService {

    constructor() {
        this.config = new Configuration()
    }

    async getAll() {
        return fetch(this.config.GENRES_URL)
            .then(response => {
                if (!response.ok) {
                    this.handleResponseError(response)
                }
                return response.json()
            }).then(json => {
                    console.log("Retrieved items:");
                    console.log(json);
                    return json;
                }
            )
            .catch(error => this.handleError(error))
    }

    async addGenre(genreName) {
        console.log("GenreService.addGenre():");
        console.log(genreName);
        const genre = {name: genreName};
        return fetch(this.config.ADD_GENRE_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(genre)
        })
            .then(response => {
                if (!response.ok) {
                    this.handleResponseError(response);
                }
                return response.json();
            })
            .catch(error => {
                this.handleError(error);
            });
    }

    handleResponseError(response) {
        throw new Error("HTTP error, status = " + response.status);
    }

    handleError(error) {
        console.log(error.message);
    }
}