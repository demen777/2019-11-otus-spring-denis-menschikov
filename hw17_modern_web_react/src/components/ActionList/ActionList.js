import React from "react";
import {NavLink} from "react-router-dom";

export default function ActionList(props) {
    const actions = props.actions;
    return (
        <div className="btn-group" role="group" aria-label="Список действий">
            {actions.map(action => <NavLink className="btn btn-secondary" key={action.text}
                                            to={action.url}>{action.text}</NavLink>)}
        </div>
    );
}