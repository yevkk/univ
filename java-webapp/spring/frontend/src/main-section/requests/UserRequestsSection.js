import React from "react";
import '../MainSection.css'
import '../OverlayForm.css'
import {
    serverURL,
    getRequestsByUser, getReturnRequestsByUser
} from "../../utils/api";

let selectedRequestID;

class RequestRow extends React.Component {
    requestID
    returnRequest

    openRequestForm() {
        let returnRequestForm = document.getElementsByClassName('ReturnRequestForm-holder')[0]
        selectedRequestID = this.requestID
        returnRequestForm.style.display = 'block'
    }

    constructor(props) {
        super(props);
        this.requestID = this.props.request.id
        console.log(props)
        console.log(props.returnRequest)
        console.log(this.props.returnRequest)
    }

    render() {
        return <tr>
            <td className="centralized">{this.props.request.datetime}</td>
            <td className="centralized">{this.props.request.book.name + ', ' + this.props.request.book.lang}</td>
            <td>{this.props.request.deliveryType.description}</td>
            <td>{this.props.request.contact}</td>
            <td className="centralized">{this.props.request.state}</td>
            {this.props.request.state.toUpperCase() === 'PROCESSED' && this.props.returnRequest === undefined ? <td>
                <div className="MainSection-button" onClick={() => this.openRequestForm()}>return</div>
            </td> : <td/>}
            {this.props.returnRequest !== undefined ?
                <td className="centralized">{this.props.returnRequest.datetime}</td> : <td/>}
            {this.props.returnRequest !== undefined ? <td className="centralized">{this.props.returnRequest.state}</td> : <td/>}
        </tr>
    }
}

class ReturnRequestForm extends React.Component {
    close() {
        document.getElementsByClassName('ReturnRequestForm-holder')[0].style.display = 'none'
    }

    async sendReturnRequest() {
        let url = new URL(`${serverURL}/return_requests`)

        let rate = document.forms.returnRequestForm.elements.rate.value

        url.searchParams.set('request_id', selectedRequestID)
        url.searchParams.set('rate', rate)

        await fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        })
        this.close();
        window.location.reload(false);
    }

    render() {
        return <div className="OverlayForm-holder ReturnRequestForm-holder">
            <div className="OverlayForm">
                <header className="OverlayForm-header">
                    Return book
                </header>
                <div className="OverlayForm-close-button" onClick={() => this.close()}>
                    X
                </div>
                <form name="returnRequestForm">
                    <label className="OverlayForm-label" htmlFor="ReturnRequestForm-rate-input">rate: </label>
                    <input className="OverlayForm-input" id="ReturnRequestForm-rate-input" name="rate" type="number" min="0" max="5" step="0.1"/>
                </form>

                <div className="OverlayForm-button" onClick={() => this.sendReturnRequest()}>
                    send
                </div>
            </div>
        </div>
    }
}

export class UserRequestsSection extends React.Component {


    componentDidMount() {
        getRequestsByUser(localStorage.getItem('user-id')).then(result => {
            let requests = result
            getReturnRequestsByUser(localStorage.getItem('user-id')).then(result => {
                let returnRequests = result
                console.log(returnRequests)
                this.setState({...this.state, requests, returnRequests})
            })
        })

        console.log(this.state.returnRequests)
    }

    constructor(props) {
        super(props)
        this.state = {
            requests: [],
            returnRequests: []
        }
    }

    render() {
        return <div className="content-wrapper">
            <ReturnRequestForm />
            <section className="MainSection">
                <header className="MainSection-header">
                    Requests
                </header>
                <table className="MainSection-table">
                    <colgroup>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>Datetime</td>
                        <td>Book</td>
                        <td>Delivery</td>
                        <td>Contact</td>
                        <td>State</td>
                        <td/>
                        <td>Return Datetime</td>
                        <td>Return state</td>
                    </tr>
                    {this.state.requests.map(request => <RequestRow request={request} returnRequest={this.state.returnRequests.find(item => item.request.id === request.id)} />)}
                    </tbody>
                </table>
            </section>
        </div>
    }
}

