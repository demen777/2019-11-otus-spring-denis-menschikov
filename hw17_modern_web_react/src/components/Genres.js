import React, {Fragment} from "react";
import {GenreService} from "../services/GenreService";
import ActionList from "./ActionList";

export default class Genres extends React.Component {
    constructor(props) {
        super(props);
        this.genreService = new GenreService();
        this.state = {
            genres: []
        }
    }

    componentDidMount = () => {
        this.getGenres();
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

    render() {
        const {genres} = this.state;
        return (
            <Fragment>
                <ActionList actions={[{url:"/genre/add", text: "Добавить"}]}/>
                <h4>Список жанров</h4>
                <table className="table table-striped">
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Наименование</th>
                    </tr>
                    </thead>
                    <tbody>
                    {genres.map((genre) =>
                        <tr key={genre.id}>
                            <td>{genre.id}</td>
                            <td>{genre.name}</td>
                        </tr>
                    )}
                    </tbody>
                </table>
            </Fragment>
        );
    }
}