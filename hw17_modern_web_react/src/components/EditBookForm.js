import React from "react";
import {BookService} from "../services/BookService";
import {GenreService} from "../services/GenreService";
import {AuthorService} from "../services/AuthorService";

export default class EditBookForm extends React.Component {
    constructor(props) {
        super(props);
        console.log(props);
        const bookId = props.match.params.bookId;
        this.bookService = new BookService();
        this.genreService = new GenreService();
        this.authorService = new AuthorService();
        // noinspection JSUnresolvedVariable
        this.state = {
            bookId: bookId,
            book: {
                id: bookId,
                name: "",
                authorId: 0,
                genreId: 0
            },
            genres: [],
            authors: []
        }
    }

    componentDidMount = () => {
        this.getBook(this.state.bookId);
        this.getGenres();
        this.getAuthors();
    };

    getBook(bookId) {
        this.bookService.getBook(bookId)
            .then(book => {
                console.log(book);
                if (book !== undefined) {
                    this.setState({
                        book: {
                            id: bookId,
                            name: book.name,
                            authorId: book.author.id,
                            genreId: book.genre.id
                        }
                    });
                }
            })
            .catch(error => console.log(error));
    }

    getGenres() {
        this.genreService.getAll()
            .then(genres => {
                console.log(genres);
                if (genres !== undefined) {
                    this.setState({genres: genres});
                }
            })
            .catch(error => console.log(error));
    }

    getAuthors() {
        this.authorService.getAll()
            .then(authors => {
                console.log(authors);
                if (authors !== undefined) {
                    this.setState({authors: authors});
                }
            })
            .catch(error => console.log(error));
    }

    editBook = event => {
        event.preventDefault();
        console.log(event.target.value);
        const jsonPromise = this.bookService.editBook(this.state.bookId, this.state.book);
        jsonPromise.then(editedBook => {
                console.log("---" + editedBook);
                if (editedBook !== undefined) {
                    console.log(editedBook);
                    window.location.href = "/books";
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        const {bookId, book, genres, authors} = this.state;
        return (
            <div>
                <h4>Изменение информации о книге</h4>
                <form onSubmit={this.editBook}>
                    <table className="paleblue">
                        <tbody>
                        <tr>
                            <td className="header">ID</td>
                            <td>{bookId}</td>
                        </tr>
                        <tr>
                            <td className="header">Наименование</td>
                            <td><input name="name" className="form-control" placeholder="Enter name"
                                       value={book.name}
                                       onChange={this.handleChange}/></td>
                        </tr>
                        <tr>
                            <td className="header">Автор</td>
                            <td>
                                <select name="authorId" value={book.authorId} onChange={this.handleChange}>
                                    {authors.map((author) =>
                                        <option key={author.id} value={author.id}>{author.name}</option>
                                    )}
                                </select>
                            </td>
                        </tr>
                        <tr>
                            <td className="header">Жанр</td>
                            <td>
                                <select name="genreId" value={book.genreId} onChange={this.handleChange}>
                                    {genres.map((genre) =>
                                        <option key={genre.id} value={genre.id}>{genre.name}</option>
                                    )}
                                </select>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                    <div className="buttons">
                        <button type="submit">Изменить</button>
                    </div>
                </form>
            </div>
        );
    }

    handleChange = event => {
        const {name, value} = event.target;
        this.setState({
            book: {...this.state.book, [name]: value}
        });
    };
}