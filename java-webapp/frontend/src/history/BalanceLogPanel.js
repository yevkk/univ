import React from "react";
import '../index.css'
import './HistoryPanel.css'
import {
    getBalanceChangelog,
    getBalanceChangelogByBookID, getBalanceChangelogInPeriod,
    getHistory,
    getHistoryByBookID,
    getHistoryInPeriod
} from "../api/api";
import {serverURL} from "../index";

class BalanceLogRow extends React.Component {
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
            <td>{this.convertDatetime(this.props.logRecord.datetime)}</td>
            <td>{this.props.logRecord.bookID}</td>
            <td>{this.props.logRecord.amount}</td>
            <td>{this.props.logRecord.comment}</td>
        </tr>
    }
}

export class BalanceChangelogPanel extends React.Component {
    componentDidMount() {
        getBalanceChangelog().then(result => {
            this.setState({...this.state, balanceChangelog: result})
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            balanceChangelog: [],
        }
    }

    showAll() {
        getBalanceChangelog().then(result => {
            this.setState({...this.state, balanceChangelog: result})
        })
    }

    showByBookID() {
        let bookID = prompt('Enter book ID: ');
        getBalanceChangelogByBookID(bookID).then(result => {
            this.setState({...this.state, balanceChangelog: result})
        })
    }

    showInPeriod() {
        let start = prompt('Enter start of period (format yyyy-mm-dd): ');
        let end = prompt('Enter end of period (format yyyy-mm-dd): ');
        getBalanceChangelogInPeriod(start, end).then(result => {
            this.setState({...this.state, balanceChangelog: result})
        })
    }

    render() {
        return <div className="content-wrapper">
            <div className="HistoryPanel">
                <header className="HistoryPanel-header">
                    Balance changelog
                    <div className="HistoryPanel-button" onClick={() => this.showAll()}>all</div>
                    <div className="HistoryPanel-button" onClick={() => this.showInPeriod()}>in period</div>
                    <div className="HistoryPanel-button" onClick={() => this.showByBookID()}>by book ID</div>
                </header>
                <table className="HistoryPanel-table">
                    <colgroup>
                        <col span="1" style={{width: "25%"}}/>
                        <col span="1" style={{width: "25%"}}/>
                        <col span="1" style={{width: "25%"}}/>
                        <col span="1" style={{width: "25%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>Datetime</td>
                        <td>Book ID</td>
                        <td>Amount</td>
                        <td>Comment</td>
                    </tr>
                    {this.state.balanceChangelog.map(item => <BalanceLogRow logRecord={item}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}
