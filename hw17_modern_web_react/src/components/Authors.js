import React, {Fragment} from "react";
import {AuthorService} from "../services/AuthorService";
import ActionList from "./ActionList";

export default class Authors extends React.Component {
    constructor(props) {
        super(props);
        this.authorService = new AuthorService();
        this.state = {
            authors: []
        }
    }

    componentDidMount = () => {
        this.getAuthors();
    };

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

    render() {
        const {authors} = this.state;
        return (
            <Fragment>
                <ActionList actions={[{url:"/author/add", text: "Добавить"}]}/>
                <h4>Список авторов</h4>
                <table className="table table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>ФИО</th>
                    </tr>
                    </thead>
                    <tbody>
                    {authors.map((author) =>
                        <tr key={author.id}>
                            <td>{author.id}</td>
                            <td>{author.name}</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </Fragment>
        );
    }
}