import React from "react";
import '../MainSection.css'
import '../OverlayForm.css'
import {getRequests, getReturnRequests, serverURL} from "../../utils/api";
import {convertDatetime} from "../../utils/utils";

class RequestRow extends React.Component {
    requestID

    constructor(props) {
        super(props);
        this.requestID = this.props.request.id
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
        }).then(() => {
            this.setState({...this.state})
            window.location.reload(false);
        })
    }

    render() {
        return <tr>
            <td className="centralized">{this.props.request.id}</td>
            <td className="centralized">{convertDatetime(this.props.request.datetime)}</td>
            <td className="centralized">{this.props.request.userID}</td>
            <td className="centralized">{this.props.request.bookID}</td>
            <td className="centralized">{this.props.request.deliveryTypeID}</td>
            <td>{this.props.request.contact}</td>
            <td className="centralized">{this.props.request.state}</td>
            {this.props.request.state !== 'PROCESSED' && this.props.request.state !== 'RETURNED' ? <td>
                <div className="MainSection-button" onClick={() => this.submitRequest()}>submit</div>
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
        }).then(() => {
            this.setState({...this.state})
            window.location.reload(false);
        })
    }

    render() {
        return <tr className="centralized">
            <td>{this.props.returnRequest.id}</td>
            <td>{convertDatetime(this.props.returnRequest.datetime)}</td>
            <td>{this.props.returnRequest.getBookRequestID}</td>
            <td>{this.props.returnRequest.state}</td>
            {this.props.returnRequest.state !== 'PROCESSED' && this.props.returnRequest.state !== 'RETURNED' ? <td>
                <div className="MainSection-button" onClick={() => this.submitRequest()}>submit</div>
            </td> : <td/>}
            <td className="white" />
        </tr>
    }
}

export class AdminRequestsSection extends React.Component {
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
            <section className="MainSection">
                <header className="MainSection-header">
                    Requests
                </header>
                <table className="MainSection-table">
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
            </section>
            <section className="MainSection">
                <header className="MainSection-header">
                    Return requests
                </header>
                <table className="MainSection-table">
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
                        <td className="white" />
                    </tr>
                    {this.state.returnRequests.map(request => <ReturnRequestRow returnRequest={request}/>)}
                    </tbody>
                </table>
            </section>
        </div>
    }
}