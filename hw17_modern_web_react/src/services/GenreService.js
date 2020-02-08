import {Configuration} from "./Configuration";
import {handleError, handleResponseError} from "./ErrorHandlers";

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

    checkStatus(response) {
        if (!response.ok) {
            return handleResponseError(response);
        }
        else {
            return Promise.resolve(response);
        }
    }

    async addGenre(genreName) {
        console.log("GenreService.addGenre():");
        console.log(genreName);
        const genre = {name: genreName};
        const response = await fetch(this.config.ADD_GENRE_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(genre)
        })
            .then(this.checkStatus)
            .catch(error => handleError(error));
        if (!response.ok) {
            await handleResponseError(response);
        }
        else {
            return response.json();
        }
    }
}