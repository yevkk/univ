import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";

export class AdminView extends React.Component {
    render() {
        return <div className="AdminView">
            <ViewHeader />
            <ViewMenu />
        </div>
    }
}
