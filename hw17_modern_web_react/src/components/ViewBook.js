import React, {Fragment} from "react";
import BookComments from "./BookComments";

export default class ViewBook extends React.Component {
    constructor(props) {
        super(props);
        console.log(props);
        const book = props.location.book;
        this.state = {
            book: book
        }
    }

    render() {
        const {book} = this.state;
        return (
            <Fragment>
                <div>
                    <h4>Информация о книге</h4>
                    <div className="form-group">
                        <label>ID
                            <span>{book.id}</span>
                        </label>
                        <label>Наименование
                            <span>{book.name}</span>
                        </label>
                        <label>Жанр
                            <span>{book.genre.name}</span>
                        </label>
                        <label>Автор
                            <span>{book.author.name}</span>
                        </label>
                    </div>
                </div>
                <BookComments bookId={book.id}/>
            </Fragment>
        );
    }
}