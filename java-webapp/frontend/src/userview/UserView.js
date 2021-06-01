import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";

export class UserView extends React.Component {
    render() {
        return <div className="UserView">
            <ViewHeader />
            <ViewMenu />
        </div>
    }
}