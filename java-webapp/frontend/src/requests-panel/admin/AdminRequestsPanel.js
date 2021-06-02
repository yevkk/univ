import React from "react";
import './AdminRequestPanel.css'
import {getRequests, getReturnRequests} from "../../api/api";
import {serverURL} from "../../index";

class RequestRow extends React.Component {
    requestID

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

    async submitRequest() {
        let url = new URL(`${serverURL}/request/update?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        url.searchParams.set('id', this.requestID)
        url.searchParams.set('state', 'PROCESSED')

        fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        }).then(() => this.setState({...this.state}))
    }

    render() {
        return <tr>
            <td>{this.props.request.id}</td>
            <td>{this.convertDatetime(this.props.request.datetime)}</td>
            <td>{this.props.request.userID}</td>
            <td>{this.props.request.bookID}</td>
            <td>{this.props.request.deliveryTypeID}</td>
            <td>{this.props.request.contact}</td>
            <td>{this.props.request.state}</td>
            {this.props.request.state !== 'PROCESSED' && this.props.request.state !== 'RETURNED' ? <td>
                <div className="AdminRequestsPanel-submit-button" onClick={() => this.submitRequest()}>submit</div>
            </td> : <td/>}
        </tr>
    }
}

class ReturnRequestRow extends React.Component {
    returnRequestID

    constructor(props) {
        super(props);
        this.returnRequestID = this.props.returnRequest.id
    }

    convertDatetime(datetime) {
        let year = datetime.date.year
        let month = datetime.date.month < 10 ? '0' + datetime.date.month : datetime.date.month
        let day = datetime.date.day < 10 ? '0' + datetime.date.day : datetime.date.day
        let hour = datetime.time.hour < 10 ? '0' + datetime.time.hour : datetime.time.hour
        let minute = datetime.time.minute < 10 ? '0' + datetime.time.minute : datetime.time.minute

        return [year, month, day].join('-') + ' ' + [hour, minute].join(':')
    }

    async submitRequest() {
        let url = new URL(`${serverURL}/return_request/update?login=${localStorage.getItem('login')}&password=${localStorage.getItem('password')}`)

        url.searchParams.set('id', this.returnRequestID)
        url.searchParams.set('state', 'PROCESSED')

        fetch(url.toString(), {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json;charset=utf-8'
            },
            body: JSON.stringify({})
        }).then(() => this.setState({...this.state}))
    }

    render() {
        return <tr>
            <td>{this.props.returnRequest.id}</td>
            <td>{this.convertDatetime(this.props.returnRequest.datetime)}</td>
            <td>{this.props.returnRequest.getBookRequestID}</td>
            <td>{this.props.returnRequest.state}</td>
            {this.props.returnRequest.state !== 'PROCESSED' && this.props.returnRequest.state !== 'RETURNED' ? <td>
                <div className="AdminRequestsPanel-submit-button" onClick={() => this.submitRequest()}>submit</div>
            </td> : <td/>}
            <td/>
        </tr>
    }
}

export class AdminRequestsPanel extends React.Component {
    componentDidMount() {
        getRequests().then(result => this.setState({...this.state, requests: result.sort((item1, item2) => item2.id - item1.id)}))
        getReturnRequests().then(result => this.setState({...this.state, returnRequests: result.sort((item1, item2) => item2.id - item1.id)}))
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
            <div className="RequestsPanel AdminRequestsPanel">
                <header className="RequestsPanel-header">
                    Requests
                </header>
                <table className="RequestsPanel-table AdminRequestsPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>Datetime</td>
                        <td>User ID</td>
                        <td>Book ID</td>
                        <td>Delivery ID</td>
                        <td>Contact</td>
                        <td>State</td>
                        <td/>
                    </tr>
                    {this.state.requests.map(request => <RequestRow request={request}/>)}
                    </tbody>
                </table>

                <header className="RequestsPanel-header">
                    Return requests
                </header>
                <table className="RequestsPanel-table AdminRequestsPanel-return-table">
                    <colgroup>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "15%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "10%"}}/>
                        <col span="1" style={{width: "20%"}}/>
                        <col span="1" style={{width: "55%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>ID</td>
                        <td>Datetime</td>
                        <td>Request ID</td>
                        <td>State</td>
                        <td/>
                        <td/>
                    </tr>
                    {this.state.returnRequests.map(request => <ReturnRequestRow returnRequest={request}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}