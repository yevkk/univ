import React from "react";
import './ViewHeader.css'
import '../index.css'

export class ViewHeader extends React.Component {
    logout() {
        localStorage.removeItem('username')
        localStorage.removeItem('email')
        localStorage.removeItem('user-id')
        localStorage.removeItem('is-admin')

        window.location.href = '/'
        this.props.keycloak.logout();
    }

    render() {
        return <header className="ViewHeader" >
            <div className="ViewHeader-content content-wrapper">
                <div className="ViewHeader-login-info">
                    user: <i>{localStorage.getItem('username')}</i> <br />
                    id: <i>{localStorage.getItem('user-id')}</i>
                </div>
                <div className="ViewHeader-logout-button" onClick={() => this.logout()}>
                    log out
                </div>
            </div>
        </header>
    }
}