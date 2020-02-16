export class Configuration {
    BASE_URL = "/api";
    GENRES_URL = this.BASE_URL + "/genres";
    ADD_GENRE_URL = this.BASE_URL + "/genre";
    DELETE_BOOK_URL = this.BASE_URL + "/book/{bookId}";
    BOOKS_URL = this.BASE_URL + "/books";
    AUTHORS_URL = this.BASE_URL + "/authors";
    ADD_AUTHOR_URL = this.BASE_URL + "/author";
    ADD_BOOK_URL = this.BASE_URL + "/book";
    EDIT_BOOK_URL = this.BASE_URL + "/book/{bookId}";
    BOOK_COMMENTS_URL = this.BASE_URL + "/book/{bookId}/comments";
    ADD_BOOK_COMMENT_URL = this.BASE_URL + "/book/{bookId}/comment";
    DELETE_BOOK_COMMENT_URL = this.BASE_URL + "/book/comment/{bookCommentId}";
    BOOK_URL = this.BASE_URL + "/book/{bookId}";
}