import React from "react";
import '../index.css'
import './HistoryPanel.css'
import {
    getBalanceChangelog,
    getBalanceChangelogByBookID, getBalanceChangelogInPeriod,
    getHistory,
    getHistoryByBookID,
    getHistoryInPeriod, getRateChangelog, getRateChangelogByBookID, getRateChangelogInPeriod
} from "../api/api";
import {serverURL} from "../index";

class RateLogRow extends React.Component {
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
            <td>{this.props.logRecord.userID}</td>
            <td>{this.props.logRecord.contribution}</td>
        </tr>
    }
}

export class RateChangelogPanel extends React.Component {
    componentDidMount() {
        getRateChangelog().then(result => {
            this.setState({...this.state, rateChangelog: result})
        })
    }

    constructor(props) {
        super(props)
        this.state = {
            rateChangelog: [],
        }
    }

    showAll() {
        getRateChangelog().then(result => {
            this.setState({...this.state, rateChangelog: result})
        })
    }

    showByBookID() {
        let bookID = prompt('Enter book ID: ');
        getRateChangelogByBookID(bookID).then(result => {
            this.setState({...this.state, rateChangelog: result})
        })
    }

    showInPeriod() {
        let start = prompt('Enter start of period (format yyyy-mm-dd): ');
        let end = prompt('Enter end of period (format yyyy-mm-dd): ');
        getRateChangelogInPeriod(start, end).then(result => {
            this.setState({...this.state, rateChangelog: result})
        })
    }

    render() {
        return <div className="content-wrapper">
            <div className="HistoryPanel">
                <header className="HistoryPanel-header">
                    Rate changelog
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
                        <td>User ID</td>
                        <td>Contribution</td>
                    </tr>
                    {this.state.rateChangelog.map(item => <RateLogRow logRecord={item}/>)}
                    </tbody>
                </table>
            </div>
        </div>
    }
}
