import React from "react";
import {BookService} from "../services/BookService";
import {GenreService} from "../services/GenreService";
import {AuthorService} from "../services/AuthorService";
import {Redirect} from "react-router-dom";

export default class AddBookForm extends React.Component {
    constructor(props) {
        super(props);
        this.bookService = new BookService();
        this.genreService = new GenreService();
        this.authorService = new AuthorService();
        this.state = {
            redirect: false,
            book: {
                name: "",
                authorId: 1,
                genreId: 1
            },
            genres: [],
            authors: []
        }
    }

    componentDidMount = () => {
        this.getGenres();
        if (this.state.genres.length > 0) {
            this.setState({
                book: {...this.state.book, genreId: this.state.genres[0].id}
            })
        }
        this.getAuthors();
        if (this.state.authors.length > 0) {
            this.setState({
                book: {...this.state.book, authorId: this.state.authors[0].id}
            })
        }
    };

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

    addBook = event => {
        event.preventDefault();
        console.log(event.target.value);
        const jsonPromise = this.bookService.addBook(this.state.book);
        jsonPromise.then(newBook => {
                console.log("---" + newBook);
                if (newBook !== undefined) {
                    console.log(newBook);
                    this.setState({redirect: true});
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        if (this.state.redirect) {
            return <Redirect push to="/books" />;
        }
        const {book, genres, authors} = this.state;
        return (
            <div>
                <h4>Добавление информации о книге</h4>
                <form onSubmit={this.addBook}>
                    <table className="paleblue">
                        <tbody>
                        <tr>
                            <td className="header">Наименование</td>
                            <td><input name="name" placeholder="Enter name" value={book.name}
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
                        <button type="submit">Добавить</button>
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