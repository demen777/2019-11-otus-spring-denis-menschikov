import {NavLink} from "react-router-dom";
import React from "react";

export default function Navigation() {
    return (
        <nav className="nav nav-pills">
            <NavLink className="nav-link" to="/books">Книги</NavLink>
            <NavLink className="nav-link" to="/authors">Авторы</NavLink>
            <NavLink className="nav-link" to="/genres">Жанры</NavLink>
        </nav>);
}