import React from "react";
import './ViewMenu.css'

export class ViewMenuItem extends React.Component {
    render() {
        return <div className="ViewMenuItem">
            <img className="ViewMenuItem-image" src={this.props.image} alt="menu-icon" />
            <p className="ViewMenuItem-text">{this.props.text}</p>
        </div>
    }

    onClick() {
        alert('aaaaaa');
    }
}