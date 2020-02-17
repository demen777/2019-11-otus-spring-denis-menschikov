import React, {Fragment} from "react";
import BookComments from "./BookComments";
import AttributeRow from "./AttributeRow";
import {BookService} from "../services/BookService";

export default class ViewBook extends React.Component {
    constructor(props) {
        super(props);
        console.log("ViewBook constructor bookId=" + props.match.params.bookId);
        this.bookService = new BookService();
        const bookId = props.match.params.bookId;
        this.state = {
            bookId: bookId,
            book: {
                name: "",
                author: {
                    name: ""
                },
                genre: {
                    name: ""
                }
            }
        }

    }

    componentDidMount = () => {
        this.getBook(this.state.bookId);
    };

    getBook(bookId) {
        this.bookService.getBook(bookId)
            .then(book => {
                console.log(book);
                if (book !== undefined) {
                    this.setState({book: book});
                }
            })
            .catch(error => console.log(error));
    }

    render() {
        const book = this.state.book;
        // noinspection JSUnresolvedVariable
        return (
            <Fragment>
                    <h4>Информация о книге</h4>
                    <table className="paleblue">
                        <tbody>
                        <AttributeRow name="ID" value={this.state.bookId}/>
                        <AttributeRow name="Наименование" value={book.name}/>
                        <AttributeRow name="Жанр" value={book.genre.name}/>
                        <AttributeRow name="Автор" value={book.author.name}/>
                        </tbody>
                    </table>
                <BookComments bookId={this.state.bookId}/>
            </Fragment>
        );
    }
}