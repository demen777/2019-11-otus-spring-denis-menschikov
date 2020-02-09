import {Configuration} from "./Configuration";
import {HttpResponseError} from "../utils/ResponseError";
import {checkResponseAndJson, getResponseAndJson} from "../utils/ResponseHandlers";

export class BookService {
    constructor() {
        this.config = new Configuration()
    }

    async getAll() {
        return fetch(this.config.BOOKS_URL)
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }

    async deleteBook(bookId) {
        console.log("BookService.deleteBook bookId=" + bookId);
        return fetch(this.config.DELETE_BOOK_URL + bookId, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(this.checkStatus);
    }

    checkStatus(response) {
        if(response.ok || response.status === 404) {
            return true;
        }
        else {
            throw new HttpResponseError(response);
        }
    }
}