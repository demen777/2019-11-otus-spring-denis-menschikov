import React from "react";
import {BookService} from "../services/BookService";
import {GenreService} from "../services/GenreService";
import {AuthorService} from "../services/AuthorService";

export default class AddBookForm extends React.Component {
    constructor(props) {
        super(props);
        this.bookService = new BookService();
        this.genreService = new GenreService();
        this.authorService = new AuthorService();
        this.state = {
            book:{
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
        if(this.state.genres.length > 0) {
            this.setState({
                book:{...this.state.book, genreId: this.state.genres[0].id}})
        }
        this.getAuthors();
        if(this.state.authors.length > 0) {
            this.setState({
                book:{...this.state.book, authorId: this.state.authors[0].id}})
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
                    window.location.href = "/books";
                }
            }
        ).catch(error => alert(error.message));
    };

    render() {
        const {book, genres, authors} = this.state;
        // noinspection JSUnresolvedVariable
        return (
            <div>
                <h4>Добавление информации о книге</h4>
                <form onSubmit={this.addBook}>
                    <div className="form-group">
                        <label>Наименование
                            <input name="name" className="form-control" placeholder="Enter name" value={book.name}
                                   onChange={this.handleChange}/>
                        </label>
                        <label>Жанр
                            <select name="genreId" value={book.genreId} onChange={this.handleChange}>
                                {genres.map((genre) =>
                                    <option value={genre.id}>{genre.name}</option>
                                )}
                            </select>
                        </label>
                        <label>Автор
                            <select name="authorId" value={book.authorId} onChange={this.handleChange}>
                                {authors.map((author) =>
                                    <option value={author.id}>{author.firstName + " " + author.surname}</option>
                                )}
                            </select>
                        </label>
                    </div>
                    <button type="submit" className="btn btn-primary">Добавить</button>
                </form>
            </div>
        );
    }

    handleChange = event => {
        const {name, value} = event.target;
        this.setState({
            book:{...this.state.book, [name]: value}});
    };
}