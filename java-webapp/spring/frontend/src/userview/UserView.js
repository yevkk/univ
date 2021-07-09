import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";
import {Switch, Route, BrowserRouter} from "react-router-dom";
import {UserBooksSection} from "../main-section/books/UserBooksSection";
import {UserRequestsSection} from "../main-section/requests/UserRequestsSection";

export class UserView extends React.Component {
    render() {
        return <div className="UserView">
            <ViewHeader keycloak={this.props.keycloak} />
            <BrowserRouter>
            <ViewMenu/>
                <Switch>
                    <Route path="/books">
                        <UserBooksSection/>
                    </Route>
                    <Route path="/requests">
                        <UserRequestsSection />
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    }
}