import React from "react";
import '../index.css'
import  './HistoryPanel.css'
import {getHistory} from "../api/api";

class HistoryRow extends React.Component {
    convertDate(date) {
        let year = date.year
        let month = date.month < 10 ? '0' + date.month : date.month
        let day = date.day < 10 ? '0' + date.day : date.day

        return [year, month, day].join('-')
    }

    render() {
        return <tr>
            <td>{this.convertDate(this.props.historyRecord.date)}</td>
            <td>{this.props.historyRecord.stats.bookID}</td>
            <td>{this.props.historyRecord.stats.amount}</td>
            <td>{this.props.historyRecord.stats.totalRequests}</td>
            <td>{this.props.historyRecord.stats.rate}</td>
        </tr>
    }
}

export class HistoryPanel extends React.Component {
    componentDidMount() {
        getHistory().then(result => {
            this.setState({...this.state, history: result})
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            history: [],
        }
    }

    showAll() {

    }

    render() {
        return <div className="content-wrapper">
            <div className="HistoryPanel">
                <header className="HistoryPanel-header">
                    Stats history
                    <div className="HistoryPanel-button" onClick={() => this.showAll()}>all</div>
                    <div className="HistoryPanel-button" onClick={() => this.showByBookID()}>in period</div>
                    <div className="HistoryPanel-button" onClick={() => this.openCreateForm()}>group by book</div>
                </header>
                <table className="HistoryPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "20%"}} />
                        <col span="1" style={{width: "20%"}} />
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>Date</td>
                        <td>Book ID</td>
                        <td>Amount</td>
                        <td>Readers</td>
                        <td>Rate</td>
                    </tr>
                    {this.state.history.map(item => <HistoryRow historyRecord={item}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}
