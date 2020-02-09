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

    async addBook(book) {
        console.log("BookService.addBook book=" + book);
        return fetch(this.config.ADD_BOOK_URL, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(book)
        })
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }

    async editBook(bookId, book) {
        console.log("BookService.addBook bookId=" + bookId + " bookId=" + bookId);
        return fetch(this.config.EDIT_BOOK_URL + bookId, {
            method: "PUT",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(book)
        })
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }
}