import React from "react";
import '../MainSection.css'
import '../OverlayForm.css'
import { getRateChangelog, getRateChangelogByBookID, getRateChangelogInPeriod, serverURL} from "../../utils/api";
import {convertDatetime} from "../../utils/utils";

class RateLogRow extends React.Component {
    render() {
        return <tr className="centralized">
            <td>{convertDatetime(this.props.logRecord.datetime)}</td>
            <td>{this.props.logRecord.bookID}</td>
            <td>{this.props.logRecord.userID}</td>
            <td>{this.props.logRecord.contribution}</td>
        </tr>
    }
}

export class RateLogSection extends React.Component {
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
            <div className="MainSection">
                <header className="MainSection-header">
                    Rate changelog
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
