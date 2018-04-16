import React from 'react';
import DatePicker from 'react-datepicker';
import * as moment from 'moment';
import 'moment/locale/fi'

import 'react-datepicker/dist/react-datepicker.css';

class ContractDate extends React.Component {
  constructor (props) {
    super(props)    
    this.state = {        
      startDate: moment()
    };
    this.handleChange = this.handleChange.bind(this);
  }

  handleChange(date) {
    this.setState({
      startDate: date
    });
  }

  render() {
    
    return <DatePicker
        selected={this.state.startDate}
        onChange={this.handleChange}
        dateFormat='LL'
    />;
  }
}

export default ContractDate