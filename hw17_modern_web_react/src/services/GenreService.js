import {Configuration} from "./Configuration";

export class GenreService {

    constructor() {
        this.config = new Configuration()
    }

    async getAll() {
        return fetch(this.config.GENRE_GET_ALL_URL)
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

    handleResponseError(response) {
        throw new Error("HTTP error, status = " + response.status);
    }

    handleError(error) {
        console.log(error.message);
    }
}