import React from 'react';
import { Collapse, Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import ContractDate from './ContractDate'
import EquipmentList from './../containers/EquipmentList'
import axios from 'axios'

export default class EquipmentForm extends React.Component {

  constructor (props) {
    super(props)    
    this.handleSearch = this.handleSearch.bind(this)
    this.handleNew = this.handleNew.bind(this)
    this.handleSave = this.handleSave.bind(this)
    this.handleCancel = this.handleCancel.bind(this)
    this.onEntering = this.onEntering.bind(this)
    this.onEntered = this.onEntered.bind(this)
    this.onChange = this.onChange.bind(this)
    this.state = {        
      collapse: false, 
      seacrh: true, 
      save: false, 
      new: true, 
      cancel: false, 
      result: 0,
      status: 'Good day!'
    }
  }

  validate(target) {
    console.log(target.id)
    let result = ''
    switch (target.id) {
      case 'equipmentNumber':
        result = 'Maximum Equipment Number length is ' 
          + target.maxLength 
            + ' characters. Current length is ' 
              + target.value.length
        break;    
      case 'address':
        result = 'Maximum IP Address length is ' 
          + target.maxLength 
            + ' characters. Current length is ' 
              + target.value.length
        break;    
      default:
        break;
    }
    return result
  }

  onEntering() {
    this.setState({ 
      status: 'Listing equipments...' 
    });
  }

  onEntered() {
    this.chattyStatus(
      this.state.result > 0 ?
        'Found ' + this.state.result + ' equipments. Edit equipment by clicking its Number link!' 
          : 'No results, please change search criteria and try again!'
    )
  }

  onChange(event) {
    this.chattyStatus(this.validate(event.target))
  }

  /**
   * Show text as status. In 15 second cycles it starts chatting 
   * until status is changed or it goes to sleep :)
   */
  chattyStatus(text) {
    this.setState({status: text})
      let firstStage = 'Humble to serve you!'
      let secondStage = 'Having a break? I\'ll load quote of the day for you!' 
      window.setTimeout(
        () => {
          if (this.state.status === text) // status has not changed 
            this.setState({status: firstStage})
          else return
          window.setTimeout(
            () => {
              if (this.state.status === firstStage) 
                this.setState({status: secondStage})
              else return 
              window.setTimeout(
                () => {
                  if (this.state.status === secondStage) {
                    axios.get('http://quotes.rest/qod.json?category=inspire')
                      .then(response => {
                        let quoteOfTheDay = response.data.contents.quotes[0]
                        this.setState(
                          {
                            status: quoteOfTheDay.quote + ' - [' + quoteOfTheDay.author + ']' 
                          }
                        );
                      })
                  } 
                }, 15000
              )
            }, 15000
          )
        }, 15000
    )
  }

  handleSearch() {
    this.setState({ 
      collapse: false,
      status: 'Searching...', 
      seacrh: false, 
      save: false, 
      new: false, 
      cancel: false,
      result: 10
    });
    // on promise
    window.setTimeout(() => {
      this.setState({ 
        collapse: true,
        status: 'Done!', 
        seacrh: true, 
        save: false, 
        new: true, 
        cancel: false
      })}, 5000)
  }

  handleSave() {
    this.setState({ 
      status: 'Saving...', 
      seacrh: true, 
      save: false, 
      new: true, 
      cancel: false
    });
    // on promise
    this.chattyStatus('Saved successfully!')
  }

  handleNew() {
    this.setState({
      collapse: false, 
      seacrh: false, 
      save: true, 
      new: false, 
      cancel: true
    });
    // on promise
    this.chattyStatus('Please fill the blanks and hit Save! Or just Cancel if you change your mind!')
  }

  handleCancel() {
    console.log("cancel")
    this.setState({
      seacrh: true, save: false, new: true, cancel: false
    });
    this.chattyStatus('Cancelled successfully!')
  }

  render() {
    return (
      <div>
        <Form>
          <FormGroup row>
            <Label sm={4} for="equipmentNumber">Equipment Number</Label>
            <Col>
              <Input autoFocus type="text" id="equipmentNumber" 
                maxLength="10" onChange={this.onChange} />
            </Col>
          </FormGroup>
          <FormGroup row>
            <Label sm={4} for="constractStartDate">Contract Start Date</Label>
            <Col>
              <ContractDate id="constractStartDate" 
                onChange={this.onChange} />
            </Col>
          </FormGroup>
          <FormGroup row>
            <Label sm={4} for="constractEndDate">Contract End Date</Label>
            <Col>
              <ContractDate id="constractEndDate" 
                onChange={this.onChange} />
            </Col>
          </FormGroup>
          <FormGroup row>
            <Label sm={4} for="address">IP Address</Label>
            <Col>
              <Input type="text" id="address" 
                maxLength="20" onChange={this.onChange} />
            </Col>
          </FormGroup>
          <FormGroup row>
          <Label sm={4} for="status">Status</Label>
            <Col>
              <Input disabled type="text" id="status" />
            </Col>
          </FormGroup>
          <h6>{this.state.status}</h6>
          <FormGroup check row>
            <Col sm={{ offset: 4 }}>
              {this.state.seacrh && 
                <Button onClick={this.handleSearch} >Search</Button>}
              {this.state.new && 
                <Button onClick={this.handleNew} >New</Button>}
              {this.state.save && 
                <Button onClick={this.handleSave} >Save</Button>}
              {this.state.cancel && 
                <Button onClick={this.handleCancel} >Cancel</Button>}
            </Col>
          </FormGroup>
          <FormGroup>
            <Collapse
              isOpen={this.state.collapse}
              onEntering={this.onEntering}
              onEntered={this.onEntered}
              onExiting={this.onExiting}
              onExited={this.onExited}
            >
              <EquipmentList/>
            </Collapse>            
          </FormGroup>
        </Form>
      </div>      
    );
  }
}