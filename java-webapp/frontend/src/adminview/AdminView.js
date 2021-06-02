import React from "react";
import {ViewHeader} from "../view-header/ViewHeader";
import {ViewMenu} from "../view-menu/ViewMenu";
import {BrowserRouter, Route, Switch} from "react-router-dom";
import {AdminBooksPanel} from "../books-panel/admin/AdminBooksPanel";
import {AdminRequestsPanel} from "../requests-panel/admin/AdminRequestsPanel";
import {DeliveryPanel} from "../delivery-panel/DeliveryPanel";
import {HistoryPanel} from "../history/HistoryPanel";
import {BalanceChangelogPanel} from "../history/BalanceLogPanel";
import {RateChangelogPanel} from "../history/RateLogPanel";

export class AdminView extends React.Component {
    render() {
        return <div className="AdminView">
            <ViewHeader/>
            <BrowserRouter>
                <ViewMenu/>
                <Switch>
                    <Route path="/delivery">
                        <DeliveryPanel />
                    </Route>
                    <Route path="/books">
                        <AdminBooksPanel/>
                    </Route>
                    <Route path="/requests">
                        <AdminRequestsPanel />
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
