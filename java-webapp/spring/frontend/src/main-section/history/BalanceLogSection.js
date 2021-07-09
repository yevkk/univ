import React from "react";
import '../MainSection.css';
import '../OverlayForm.css'
import {
    getBalanceChangelog,
    getBalanceChangelogByBookID, getBalanceChangelogInPeriod
} from "../../utils/api";
import {convertDatetime} from "../../utils/utils";
class BalanceLogRow extends React.Component {
    render() {
        return <tr>
            <td className="centralized">{this.props.logRecord.datetime}</td>
            <td className="centralized">{this.props.logRecord.book.name + ', ' + this.props.logRecord.book.lang}</td>
            <td className="centralized">{this.props.logRecord.amount}</td>
            <td>{this.props.logRecord.comment}</td>
        </tr>
    }
}

export class BalanceLogSection extends React.Component {
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
        let start = prompt('Enter start of period (format yyyy-mm-ddThh:mm:ss): ');
        let end = prompt('Enter end of period (format yyyy-mm-ddThh:mm:ss): ');
        getBalanceChangelogInPeriod(start, end).then(result => {
            this.setState({...this.state, balanceChangelog: result})
        })
    }

    render() {
        return <div className="content-wrapper">
            <div className="MainSection">
                <header className="MainSection-header">
                    Balance changelog
                    <div className="MainSection-header-button" onClick={() => this.showAll()}>all</div>
                    <div className="MainSection-header-button" onClick={() => this.showInPeriod()}>in period</div>
                    <div className="MainSection-header-button" onClick={() => this.showByBookID()}>by book ID</div>
                </header>
                <table className="MainSection-table">
                    <colgroup>
                        <col span="1" style={{width: "25%"}}/>
                        <col span="1" style={{width: "25%"}}/>
                        <col span="1" style={{width: "25%"}}/>
                        <col span="1" style={{width: "25%"}}/>
                    </colgroup>

                    <tbody>
                    <tr>
                        <td>Datetime</td>
                        <td>Book</td>
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
