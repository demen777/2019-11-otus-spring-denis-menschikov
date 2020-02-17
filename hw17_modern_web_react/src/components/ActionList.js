import React from "react";
import {NavLink} from "react-router-dom";

export default function ActionList(props) {
    const actions = props.actions;
    return (
        <div className="actions" role="group" aria-label="Список действий">
            {actions.map(action => <NavLink className="action" key={action.text}
                                            to={action.url}>{action.text}</NavLink>)}
        </div>
    );
}