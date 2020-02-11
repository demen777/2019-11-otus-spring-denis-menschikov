import {NavLink} from "react-router-dom";
import React from "react";

export default function Navigation() {
    return (
        <nav>
            <NavLink className="action" to="/books">Книги</NavLink>
            <NavLink className="action" to="/authors">Авторы</NavLink>
            <NavLink className="action" to="/genres">Жанры</NavLink>
        </nav>);
}