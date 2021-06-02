import React from "react";
import '../RequestsPanel.css'
import './UserRequestsPanel.css'
import {getBooks, getRequests, getReturnRequests, getDeliveryTypes} from "../../api/api-for-user";
import {serverURL} from "../../index";

let selectedRequestID;

class RequestRow extends React.Component {
    requestID

    openRequestForm() {
        let returnRequestForm = document.getElementsByClassName('ReturnRequestForm-holder')[0]
        selectedRequestID = this.requestID
        returnRequestForm.style.display = 'block'
    }

    constructor(props) {
        super(props);
        this.requestID = this.props.request.id
    }

    convertDatetime(datetime) {
        let year = datetime.date.year
        let month = datetime.date.month < 10 ? '0' + datetime.date.month : datetime.date.month
        let day = datetime.date.day < 10 ? '0' + datetime.date.day : datetime.date.day
        let hour = datetime.time.hour < 10 ? '0' + datetime.time.hour : datetime.time.hour
        let minute = datetime.time.minute < 10 ? '0' + datetime.time.minute : datetime.time.minute

        return [year, month, day].join('-') + ' ' + [hour, minute].join(':')
    }

    render() {
        return <tr>
            <td>{this.convertDatetime(this.props.request.datetime)}</td>
            <td>{this.props.request.bookName}</td>
            <td>{this.props.request.deliveryTypeText}</td>
            <td>{this.props.request.contact}</td>
            <td>{this.props.request.state}</td>
            {this.props.request.state === 'PROCESSED' ? <td>
                <div className="UserRequestsPanel-return-button" onClick={() => this.openRequestForm()}>return</div>
            </td> : <td/>}
            {this.props.request.returnRequest !== undefined ?
                <td>{this.convertDatetime(this.props.request.returnRequest.datetime)}</td> : <td/>}
            {this.props.request.returnRequest !== undefined ? <td>{this.props.request.returnRequest.state}</td> : <td/>}
        </tr>
    }
}

class ReturnRequestForm extends React.Component {
    async sendReturnRequest() {
        let url = new URL(`${serverURL}/return_request/add?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

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
        document.getElementsByClassName('ReturnRequestForm-holder')[0].style.display = 'none'
    }

    render() {
        return <div className="ReturnRequestForm-holder">
            <div className="ReturnRequestForm">
                <header className="ReturnRequestForm-header">
                    Request book
                </header>
                <form name="returnRequestForm">
                    <label className="ReturnRequestForm-label" htmlFor="ReturnRequestForm-rate-input">rate: </label>
                    <input className="ReturnRequestForm-input" id="ReturnRequestForm-rate-input" name="rate" type="number" min="0" max="5" step="0.1"/>
                </form>

                <div className="ReturnRequestForm-button" onClick={() => this.sendReturnRequest()}>
                    send
                </div>
            </div>
        </div>
    }
}

export class UserRequestsPanel extends React.Component {
    componentDidMount() {
        let requests;

        getRequests().then(result => {
            requests = result
            getReturnRequests().then(result => {
                let returnRequests = result;
                requests.forEach(request => request.returnRequest = returnRequests.find(item => item.getBookRequestID === request.id))
                getBooks().then(result => {
                    let books = result;
                    requests.forEach(request => request.bookName = books.find(item => item.id === request.bookID).name)
                    getDeliveryTypes().then(result => {
                        let deliveryTypes = result;
                        requests.forEach(request => request.deliveryTypeText = deliveryTypes.find(item => item.id === request.deliveryTypeID).description)
                        this.setState({...this.state, requests})
                    })
                })
            })
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            requests: [],
        }
    }

    render() {
        return <div className="content-wrapper">
            <ReturnRequestForm />
            <div className="RequestsPanel UserRequestsPanel">
                <header className="RequestsPanel-header">
                    Requests
                </header>
                <table className="RequestsPanel-table UserRequestsPanel-table">
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
                    {this.state.requests.map(request => <RequestRow request={request}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}

