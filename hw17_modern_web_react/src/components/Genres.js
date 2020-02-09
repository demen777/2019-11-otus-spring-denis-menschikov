import React, {Fragment} from "react";
import {NavLink} from "react-router-dom";
import {GenreService} from "../services/GenreService";

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
                <div className="btn-group" role="group" aria-label="Список действий">
                    <NavLink className="btn btn-secondary" to="/genre/add">Добавить</NavLink>
                </div>
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