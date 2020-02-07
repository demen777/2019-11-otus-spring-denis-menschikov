import React from "react";
import {NavLink} from "react-router-dom";

export default class Genres extends React.Component {
    render() {
        return (
            <div>
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
                    <tr>
                        <td>1</td>
                        <td>Лев</td>
                    </tr>
                    </tbody>
                </table>
            </div>
        );
    }
}