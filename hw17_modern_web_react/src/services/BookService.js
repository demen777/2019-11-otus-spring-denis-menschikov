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
        const url = this.config.DELETE_BOOK_URL.replace("{bookId}", bookId);
        return fetch(url, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(this.checkStatus);
    }

    checkStatus(response) {
        if (response.ok) {
            return true;
        } else {
            throw new HttpResponseError(response);
        }
    }

    async getBookComments(bookId) {
        console.log("BookService.getBookComments bookId=" + bookId);
        const url = this.config.BOOK_COMMENTS_URL.replace("{bookId}", bookId);
        return fetch(url, {
            method: "GET",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(getResponseAndJson)
            .then(checkResponseAndJson);
    }

    async addBookComment(bookId, commentText) {
        console.log("BookService.addBookComment bookId=" + bookId + " commentText=" + commentText);
        const url = this.config.ADD_BOOK_COMMENT_URL.replace("{bookId}", bookId);
        return fetch(url, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify({text: commentText})
        })
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
    }

    async deleteBookComment(bookCommentId) {
        console.log("BookService.deleteBookComment bookCommentId=" + bookCommentId);
        const url = this.config.DELETE_BOOK_COMMENT_URL.replace("{bookCommentId}", bookCommentId);
        return fetch(url, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json"
            }
        }).then(this.checkStatus);
    }

    async getBook(bookId) {
        console.log("BookService.getBook book=" + bookId);
        const url = this.config.BOOK_URL.replace("{bookId}", bookId);
        return fetch(url)
            .then(getResponseAndJson)
            .then(checkResponseAndJson);
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
        console.log("BookService.editBook bookId=" + bookId + " book=" + book);
        const url = this.config.EDIT_BOOK_URL.replace("{bookId}", bookId);
        return fetch(url, {
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