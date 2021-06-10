import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {AdminBooksSection} from "../main-section/books/AdminBooksSection";
import {HistoryPanel} from "../history/HistoryPanel";
import {BalanceChangelogPanel} from "../history/BalanceLogPanel";
import {RateChangelogPanel} from "../history/RateLogPanel";
import {AdminRequestsSection} from "../main-section/requests/AdminRequestsSection";
import {DeliveryTypeSection} from "../main-section/delivery/DeliveryTypeSection";

export class AdminView extends React.Component {
    render() {
        return <div className="AdminView printable">
            <ViewHeader/>
            <BrowserRouter>
                <ViewMenu/>
                <Switch>
                    <Route path="/books">
                        <AdminBooksSection />
                    </Route>
                    <Route path="/requests">
                        <AdminRequestsSection />
                    </Route>
                    <Route path="/delivery">
                        <DeliveryTypeSection />
                    </Route>
                    <Route path="/stats_history">
                        <HistoryPanel />
                    </Route>
                    <Route path="/balance_log">
                        <BalanceChangelogPanel />
                    </Route>
                    <Route path="/rate_log">
                        <RateChangelogPanel />
                    </Route>
                </Switch>
            </BrowserRouter>
        </div>
    }
}
