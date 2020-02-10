export class Configuration {
    BASE_URL = "/api";
    GENRES_URL = this.BASE_URL + "/genres";
    ADD_GENRE_URL = this.BASE_URL + "/genre/add";
    DELETE_BOOK_URL = this.BASE_URL + "/book/delete/";
    BOOKS_URL = this.BASE_URL + "/books";
    AUTHORS_URL = this.BASE_URL + "/authors";
    ADD_AUTHOR_URL = this.BASE_URL + "/author/add";
    ADD_BOOK_URL = this.BASE_URL + "/book/add";
    EDIT_BOOK_URL = this.BASE_URL + "/book/edit/";
    BOOK_COMMENTS_URL  = this.BASE_URL + "/book/{bookId}/comments";
    ADD_BOOK_COMMENT_URL= this.BASE_URL + "/book/{bookId}/comment/add";
    DELETE_BOOK_COMMENT_URL = this.BASE_URL + "/book/comment/delete/";;
}