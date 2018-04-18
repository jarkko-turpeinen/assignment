import React from 'react'
import { Collapse, Col, Button, Form, FormGroup, Label, Input } from 'reactstrap';
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
    this.editEquipment = this.editEquipment.bind(this)
    this.state = {
      equipment: {_id: null, _rev: null, equipmentNumber: null, address: null, contractStartDate: null, contractEndDate: null, status: null},
      collapse: false, 
      seacrh: true, 
      save: false, 
      new: true, 
      cancel: false,
      status: 'Good day! Type seach criteria and hit Enter!',
      result: []
    }
  }

  /**
   * Validates and gives feedback to user accordingly
   * @param {target is form field for validation} target 
   */
  validate(target) {
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

  /**
   * Result set is table inside Collapse object 
   * and this is its event when it is collapsing
   */
  onEntering() {
    this.setState({ 
      status: 'Listing equipments...' 
    });
  }

  /**
   * Result set is table inside Collapse object 
   * and this is its event when it is collapsed
   */
  onEntered() {
    this.chattyStatus(
      this.state.result.length > 0 ?
        'Found ' + this.state.result.length + ' equipment(s). Edit equipment by clicking its Number link!' 
          : 'No results, please change search criteria and try again!'
    )
  }

  onChange(event) {
    let change = this.state.equipment
    change[event.target.id] = event.target.value
    this.setState(change)
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

  /**
   * Search button clicked or Enter key pressed on search mode.
   * Searches by equipment number 
   * or when it's null, searches random equipments 
   * until it hits the limit.
   * @param {onSubmit event that is prevented} event 
   */
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
        let url = null
        if (equipmentNumber) // return a document
          url = process.env.REACT_APP_REST_ENDPOINT_EQUIPMENT + equipmentNumber
        else // return random documents until hits the limit
          url = process.env.REACT_APP_REST_ENDPOINT_EQUIPMENT_LIMIT 
        let response = await axios.get(url)
        this.setState({
          result: response.data,
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
    search(this.state.equipment.equipmentNumber)
  }

  /**
   * Save button clicked or Enter key pressed on new mode.
   * Read form fields into equipment object and posts it to service
   * @param {onSubmit event that is prevented} event 
   */
  handleSave(event) {
    event.preventDefault()    
    this.setState({ 
      status: 'Saving...', 
      seacrh: true, 
      save: false, 
      new: true, 
      cancel: false
    });

    let save = async () => {
      try {
        let url = process.env.REACT_APP_REST_ENDPOINT_EQUIPMENT
        await axios.post(url, this.state.equipment)
        this.setState({
          result: [],
          seacrh: true, 
          save: false, 
          new: true, 
          cancel: false,
          status: 'Saved successfully!'
        })
      } catch (error) {
        console.log(error)
        this.chattyStatus('Save failed because of an ' + error)
      }
    }
    save()
  }

  /**
   * New equipment mode is just buttons state
   */
  handleNew() {
    this.setState({
      equipment: {_id: null, _rev: null, equipmentNumber: null, address: null, contractStartDate: null, contractEndDate: null, status: null},
      collapse: false, 
      seacrh: false, 
      save: true, 
      new: false, 
      cancel: true
    });
    this.chattyStatus('Please fill the blanks and hit Enter to save or Esc to cancel!')
  }

  /**
   * Cancel editing mode by nulling _id and _rev 
   * and resets buttons to default
   */
  handleCancel() {
    this.setState({
      seacrh: true, save: false, new: true, cancel: false, _id: null, _rev: null
    });
    this.chattyStatus('Cancelled successfully!')
  }

  /**
   * Editing mode is on when equipments Cloudant id and rev are known.
   * In others words service then makes update instead of save.
   * @param {td element that has _id and _rev in its id attribute} event 
   */
  editEquipment(event) {
    let equipment = this.state.result.find(
      equipment => {return equipment._id = event.target.id
    })
    this.setState({
      equipment: equipment,
      collapse: false, 
      seacrh: false, 
      save: true, 
      new: false, 
      cancel: true
    });
    this.chattyStatus('Edit and hit Enter to save or Esc to cancel!')
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
                maxLength="10" onChange={this.onChange}
                value={this.state.equipment.equipmentNumber}
              />
            </Col>
          </FormGroup>
          <FormGroup row>
            <Label sm={4} for="contractStartDate">Contract Start Date</Label>
            <Col>
              <Input id="contractStartDate" ref="contractStartDate" 
                onChange={this.onChange} disabled={this.state.seacrh}
                value={this.state.equipment.contractStartDate}
              />
            </Col>
          </FormGroup>
          <FormGroup row>
            <Label sm={4} for="contractEndDate">Contract End Date</Label>
            <Col>
              <Input id="contractEndDate" ref="contractEndDate"
                onChange={this.onChange} disabled={this.state.seacrh}
                value={this.state.equipment.contractEndDate}                
              />
            </Col>
          </FormGroup>
          <FormGroup row>
            <Label sm={4} for="address">IP Address</Label>
            <Col>
              <Input type="text" id="address" ref="address"
                maxLength="20" onChange={this.onChange} 
                disabled={this.state.seacrh}
                value={this.state.equipment.address}
              />
            </Col>
          </FormGroup>
          <FormGroup row>
          <Label sm={4} for="status">Status</Label>
            <Col>
              <Input disabled={this.state.seacrh} onChange={this.onChange}
                maxLength="10" type="text" id="status" ref="status" 
                value={this.state.equipment.status}
              />
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
              <EquipmentList equipments={this.state.result} editEquipment={this.editEquipment}/>
            </Collapse>            
          </FormGroup>
        </Form>
      </div>      
    );
  }
}