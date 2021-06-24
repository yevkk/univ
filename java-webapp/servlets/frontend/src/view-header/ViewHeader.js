import React from "react";
import './ViewHeader.css'
import '../index.css'

export class ViewHeader extends React.Component {
    render() {
        return <header className="ViewHeader" >
            <div className="ViewHeader-content content-wrapper">
                <div className="ViewHeader-login-info">
                    user: <i>{localStorage.getItem('login')}</i> <br />
                    role: <i>{localStorage.getItem('user-role')}</i>
                </div>
                <div className="ViewHeader-logout-button" onClick={this.logout}>
                    log out
                </div>
            </div>
        </header>
    }

    logout() {
        localStorage.removeItem('login')
        localStorage.removeItem('password')
        localStorage.removeItem('user-role')

        window.location.href = '/login'
    }
}