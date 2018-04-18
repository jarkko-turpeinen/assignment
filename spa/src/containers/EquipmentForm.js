import React from 'react';
import ReactDOM from 'react-dom'
import { Collapse, Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';
import ContractDate from './../components/ContractDate'
import EquipmentList from './../components/EquipmentList'
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
      status: 'Good day! Type seach criteria and hit Enter!',
      result: []
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
      this.state.result.length > 0 ?
        'Found ' + this.state.result.length + ' equipment(s). Edit equipment by clicking its Number link!' 
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

  handleSearch(event) {
    event.preventDefault()    
    this.setState({ 
      collapse: false,
      status: 'Searching...', 
      seacrh: false, 
      save: false, 
      new: false, 
      cancel: false,
      result: []
    });
    
    let search = async (equipmentNumber) => {
      try {
        let url = process.env.REACT_APP_REST_ENDPOINT_EQUIPMENT + equipmentNumber
        console.log(url)
        let response = await axios.get(url)
        console.log(response)
        this.setState({
          result: JSON.parse(response),
          collapse: true,
          seacrh: true, 
          save: false, 
          new: true, 
          cancel: false
        })
      } catch (error) {
        console.log(error)
      }
      this.setState({
        collapse: true,
        seacrh: true, 
        save: false, 
        new: true, 
        cancel: false
      })
    }  
    search(ReactDOM.findDOMNode(this.refs.equipmentNumber).value)
  }

  handleSave(event) {
    event.preventDefault()    
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
    this.chattyStatus('Please fill the blanks and hit Enter to save or Esc to cancel!')
  }

  handleCancel() {
    this.setState({
      seacrh: true, save: false, new: true, cancel: false
    });
    this.chattyStatus('Cancelled successfully!')
  }

  render() {
    return (
      <div>
        <Form onSubmit={ this.state.seacrh ? this.handleSearch : this.handleSave }
          onKeyDown={
            (event) => {
              if (this.state.save & event.keyCode === 27) {
                this.handleCancel()
              }
            }
          }>
          <FormGroup row>
            <Label sm={4} for="equipmentNumber">Equipment Number</Label>
            <Col>
              <Input autoFocus type="text" id="equipmentNumber" ref="equipmentNumber"
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
                <Button color="primary" type="submit">Search</Button>}{' '}
              {this.state.new && 
                <Button color="secondary" 
                  onClick={this.handleNew}>New</Button>}{' '}
              {this.state.save && 
                <Button color="primary" type="submit">Save</Button>}{' '}
              {this.state.cancel && 
                <Button color="secondary" 
                  onClick={this.handleCancel} >Cancel</Button>}
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
              <EquipmentList equipments={this.state.result}/>
            </Collapse>            
          </FormGroup>
        </Form>
      </div>      
    );
  }
}